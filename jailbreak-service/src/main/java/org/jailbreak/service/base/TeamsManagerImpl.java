package org.jailbreak.service.base;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.TeamsFilters;
import org.jailbreak.service.ServiceConfiguration;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.db.dao.TeamsDAO;
import org.jailbreak.service.errors.AppException;
import org.jailbreak.service.helpers.DistanceHelper;

import com.github.slugify.Slugify;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.newrelic.deps.com.google.common.collect.Lists;

public class TeamsManagerImpl implements TeamsManager {
	
	private final TeamsDAO dao;
	private final CheckinsManager checkinsManager;
	private final Slugify slugify;
	private final DistanceHelper distanceHelper;
	private final double startLat;
	private final double startLon;
	
	@Inject
	public TeamsManagerImpl(TeamsDAO dao,
			CheckinsManager checkinsManager,
			Slugify slugify,
			DistanceHelper distanceHelper,
			ServiceConfiguration config) {
		this.dao = dao;
		this.checkinsManager = checkinsManager;
		this.slugify = slugify;
		this.distanceHelper = distanceHelper;
		this.startLat = config.getJailbreakSettings().getStartLat();
		this.startLon = config.getJailbreakSettings().getStartLon();
	}
	
	@Override
	public Optional<Team> getRawTeam(int id) {
		return dao.getTeam(id);
	}

	@Override
	public Optional<Team> getTeam(int id) {
		Optional<Team> maybeTeam = dao.getTeam(id);
		
		if (maybeTeam.isPresent()) {
			Optional<Checkin> maybeCheckin = checkinsManager.getLastTeamCheckin(id);
			if (maybeCheckin.isPresent()) {
				Team team = maybeTeam.get().toBuilder().setLastCheckin(maybeCheckin.get()).build();
				maybeTeam = Optional.of(team);
			}
		}
		
		return maybeTeam;
	}
	
	@Override
	public Optional<Team> getTeamSlug(String slug) {
		Optional<Team> maybeTeam = dao.getTeamSlug(slug);
		
		if (maybeTeam.isPresent()) {
			Team team = maybeTeam.get();
			Optional<Checkin> maybeCheckin = checkinsManager.getLastTeamCheckin(team.getId());
			if (maybeCheckin.isPresent()) {
				Team result = team.toBuilder().setLastCheckin(maybeCheckin.get()).build();
				maybeTeam = Optional.of(result);
			}
		}
		
		return maybeTeam;
	}
	
	@Override
	public Optional<Team> getLimitedTeam(int id) {
		return dao.getLimitedTeam(id);
	}

	@Override
	public List<Team> getAllTeams() {
		List<Team> teams = dao.getAllTeams();
		
		return annotateTeamsWithCheckins(teams);
	}
	
	@Override
	public List<Team> getTeams(int limit, Optional<Integer> page) {
		return this.getTeams(limit, page, TeamsFilters.getDefaultInstance());
	}
	
	@Override
	public List<Team> getTeams(int limit, Optional<Integer> page, TeamsFilters filters) {
		List<Team> teams;
		try {
			teams = dao.getFilteredTeams(limit, page, filters);
		} catch (SQLException e) {
			throw new AppException("Database error getting teams", e);
		}
		
		return annotateTeamsWithCheckins(teams);
	}
	
	@Override
	public List<Team> getTeamsByLastCheckin() {
		List<Team> teams = dao.getAllTeams();
		
		List<Team> teamsAnnotated = annotateTeamsWithCheckins(teams);
		
		Collections.sort(teamsAnnotated, new Comparator<Team>() {
			@Override
			public int compare(Team t1, Team t2) {
				if (!t1.hasLastCheckin()) {
					return 1;
				}
				
				if (!t2.hasLastCheckin()) {
					return -1;
				}
				
				Long time1 = t1.getLastCheckin().getTime();
				Long time2 = t2.getLastCheckin().getTime();
				return time1.compareTo(time2);
			}
		});
		
		return teamsAnnotated;
	}
	
	@Override
	public HashMap<Integer, Team> getLimitedTeams(Set<Integer> ids) {
		if (ids.isEmpty()) {
			return Maps.newHashMap();
		}
		List<Team> teams = dao.getLimitedTeams(ids);
		HashMap<Integer, Team> map = Maps.newHashMapWithExpectedSize(teams.size());
		
		for (Team team : teams) {
			map.put(team.getId(), team);
		}
		
		return map;
	}
	
	@Override
	public int getTotalCount(TeamsFilters filters) {
		try {
			return dao.countFilteredTeams(filters);
		} catch (SQLException e) {
			throw new AppException("Error counting the number of teams", e);
		}
	}
	
	@Override
	public Team addTeam(Team team) {
		Team.Builder builder = team.toBuilder();
		
		if (!team.hasSlug()) {
			String slug = slugify.slugify(team.getTeamName());
			builder.setSlug(slug);
		}
		
		team = builder.build();
		int new_id = dao.insert(team);
		
		// insert a new checkin to start the team off
		Checkin checkin = Checkin.newBuilder()
				.setLocation("Collins Barracks")
				.setStatus("Getting ready to go!")
				.setLat(startLat)
				.setLon(startLon)
				.setTime((int) (System.currentTimeMillis() / 1000L))
				.setTeamId(new_id)
				.setDistanceToX(distanceHelper.distanceToX(startLat, startLon))
				.build();
		
		checkinsManager.createCheckin(checkin);
		
		// return new full team object
		return dao.getTeam(new_id).get();
	}
	
	@Override
	public Optional<Team> updateTeam(Team team) {
		int result = dao.update(team);
		if (result == 0) {
			return Optional.of(team);
		} else {
			return Optional.absent();
		}
	}
	
	@Override
	public Optional<Team> patchTeam(Team newTeam) {
		Optional<Team> maybeCurrent = dao.getTeam(newTeam.getId());
		if (maybeCurrent.isPresent()) {
			Team merged = maybeCurrent.get().toBuilder().mergeFrom(newTeam).build();
			dao.update(merged);
			return Optional.of(merged);
		}
		return Optional.absent(); // returns Optional.absent if there is no team to update
	}
	
	@Override
	public void deleteTeam(int id) {
		this.dao.delete(id);
	}
	
	@Override
	public int updateAllTeamPositions(int teamIdCausedUpdate) {
		List<Team> teamsAnnotated = getAllTeams(); // get teams annotated with last chekin information
		
		// sort teams by distance to X
		Collections.sort(teamsAnnotated, new Comparator<Team>() {
			@Override
			public int compare(Team t1, Team t2) {
				if (!t1.hasLastCheckin()) {
					return 1;
				}
				
				if (!t2.hasLastCheckin()) {
					return -1;
				}
				
				Double distance1 = Double.valueOf(t1.getLastCheckin().getDistanceToX());
				Double distance2 = Double.valueOf(t2.getLastCheckin().getDistanceToX());
				return distance1.compareTo(distance2);
			}
		});
		
		// update teams that have changed position in the race (cry)
		int newPosition = 0;
		
		int nextPosition = 1;
		for (Team team : teamsAnnotated) {
			if (team.getPosition() != nextPosition) {
				dao.updateTeamPosition(team.getId(), nextPosition);
			}
			
			if (team.getId() == teamIdCausedUpdate) {
				newPosition = nextPosition;
			}
			
			nextPosition++;
		}
		
		return newPosition;
	}
	
	private List<Team> annotateTeamsWithCheckins(List<Team> teams) {
		Set<Integer> ids = lastCheckinIds(teams);
		HashMap<Integer, Checkin> map = checkinsManager.getCheckins(ids);
		
		List<Team> newTeams = Lists.newArrayListWithCapacity(teams.size());
		for (Team team : teams) {
			if (team.hasLastCheckinId()) {
				int lastCheckinId = team.getLastCheckinId();
				if (map.containsKey(lastCheckinId)) {
					newTeams.add(team.toBuilder().setLastCheckin(map.get(lastCheckinId)).build());
				}
			} else {
				newTeams.add(team);
			}
		}
		
		return newTeams;
	}
	
	private Set<Integer> lastCheckinIds(List<Team> teams) {
		Set<Integer> ids = Sets.newHashSetWithExpectedSize(teams.size());
		for (Team team : teams) {
			if (team.hasLastCheckinId()) {
				ids.add(team.getLastCheckinId());
			}
		}
		return ids;
	}
}
