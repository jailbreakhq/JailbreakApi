package org.jailbreak.service.core;

import java.util.List;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.TeamsFilters;

import com.google.common.base.Optional;

public interface TeamsManager {
	
	public Optional<Team> getTeam(int id);
	public Optional<Team> getTeamSlug(String slug);
	public List<Team> getTeams();
	public List<Team> getTeams(int limit, TeamsFilters filters);
	public Team addTeam(Team team);
	public Optional<Team> updateTeam(Team team);
	public Optional<Team> patchTeam(Team team);
	public void deleteTeam(int id);
	public List<Team> getTopTenTeams();
	
}
