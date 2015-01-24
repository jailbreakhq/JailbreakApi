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
		int new_id = dao.addTeam(team);
		return team.toBuilder().setId(new_id).build();
	}
	
	@Override
	public Optional<Team> updateTeam(int id, Team team) {
		int result = dao.updateTeam(id, team);
		if (result == 0) {
			return Optional.of(team);
		} else {
			return Optional.absent();
		}
	}
	
	@Override
	public Optional<Team> patchTeam(int id, Team newTeam) {
		Optional<Team> maybeCurrent = dao.getTeam(newTeam.getId());
		if (maybeCurrent.isPresent()) {
			Team merged = maybeCurrent.get().toBuilder().mergeFrom(newTeam).build();
			dao.updateTeam(id, merged);
			return Optional.of(merged);
		}
		return Optional.absent(); // returns Optional.absent if there is no team to update
	}
}
