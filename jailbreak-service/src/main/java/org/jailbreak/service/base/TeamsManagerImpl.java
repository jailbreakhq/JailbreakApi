package org.jailbreak.service.base;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.TeamsFilters;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.db.TeamsDAO;
import org.jailbreak.service.errors.AppException;
import org.jailbreak.service.helpers.DistanceHelper;

import com.github.slugify.Slugify;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;
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
			@Named("jailbreak.startLocationLat") double startLat,
			@Named("jailbreak.startLocationLon") double startLon) {
		this.dao = dao;
		this.checkinsManager = checkinsManager;
		this.slugify = slugify;
		this.distanceHelper = distanceHelper;
		this.startLat = startLat;
		this.startLon = startLon;
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
		return dao.getTeamSlug(slug);
	}

	@Override
	public List<Team> getTeams() {
		List<Team> teams = dao.getTeams();
		
		return annotateTeamsWithCheckins(teams);
	}
	
	@Override
	public List<Team> getTeams(int limit, TeamsFilters filters) {
		List<Team> teams;
		try {
			teams = dao.getFilteredTeams(limit, filters);
		} catch (SQLException e) {
			throw new AppException("Database error getting teams", e);
		}
		
		return annotateTeamsWithCheckins(teams);
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
	
	private List<Team> annotateTeamsWithCheckins(List<Team> teams) {
		List<Integer> ids = lastCheckinIds(teams);
		List<Checkin> checkins = checkinsManager.getCheckins(ids);
		
		HashMap<Integer, Checkin> map = Maps.newHashMap();
		for (Checkin checkin : checkins) {
			map.put(checkin.getId(), checkin);
		}
		
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
	
	private List<Integer> lastCheckinIds(List<Team> teams) {
		List<Integer> ids = Lists.newArrayListWithCapacity(teams.size());
		for (Team team : teams) {
			if (team.hasLastCheckinId()) {
				ids.add(team.getLastCheckinId());
			}
		}
		return ids;
	}
}
