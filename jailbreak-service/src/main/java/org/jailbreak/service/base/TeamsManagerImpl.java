package org.jailbreak.service.base;

import java.util.List;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.db.TeamsDAO;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class TeamsManagerImpl implements TeamsManager {
	
	private final TeamsDAO dao;
	
	@Inject
	public TeamsManagerImpl(TeamsDAO dao) {
		this.dao = dao;
	}

	@Override
	public Optional<Team> getTeam(int id) {
		return dao.getTeam(id);
	}

	@Override
	public List<Team> getTeams() {
		return dao.getTeams();
	}
	
	@Override
	public Team addTeam(Team team) {
		if (!team.hasCurrentLat() && !team.hasCurrentLon()) {
			Team.Builder builder = team.toBuilder();
			builder.setCurrentLat(team.getStartLat());
			builder.setCurrentLon(team.getCurrentLon());
			team = builder.build();
		}

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
