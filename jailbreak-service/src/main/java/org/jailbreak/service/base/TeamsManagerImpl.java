package org.jailbreak.service.base;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.TeamsFilters;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.db.TeamsDAO;

import com.github.slugify.Slugify;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TeamsManagerImpl implements TeamsManager {
	
	private final TeamsDAO dao;
	private final Slugify slugify;
	private final double startLat;
	private final double startLon;
	
	@Inject
	public TeamsManagerImpl(TeamsDAO dao,
			Slugify slugify,
			@Named("jailbreak.startLocationLat") double startLat,
			@Named("jailbreak.startLocationLon") double startLon) {
		this.dao = dao;
		this.slugify = slugify;
		this.startLat = startLat;
		this.startLon = startLon;
	}

	@Override
	public Optional<Team> getTeam(int id) {
		return dao.getTeam(id);
	}
	
	@Override
	public Optional<Team> getTeamSlug(String slug) {
		return dao.getTeamSlug(slug);
	}

	@Override
	public List<Team> getTeams() {
		return dao.getTeams();
	}
	
	@Override
	public List<Team> getTeams(int limit, TeamsFilters filters) {
		try {
			return dao.getFilteredTeams(limit, filters);
		} catch (SQLException e) {
			throw new WebApplicationException();
		}
	}
	
	@Override
	public List<Team> getTopTenTeams() {
		return dao.getTopTenTeams();
	}
	
	@Override
	public Team addTeam(Team team) {
		Team.Builder builder = team.toBuilder();
		
		if (!team.hasCurrentLat()) {
			builder.setCurrentLat(this.startLat);
		}
		
		if (!team.hasCurrentLon()) {
			builder.setCurrentLon(this.startLon);
		}
		
		if (!team.hasSlug()) {
			String slug = slugify.slugify(team.getTeamName());
			builder.setSlug(slug);
		}
		
		team = builder.build();
		int new_id = dao.insert(team);
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
}
