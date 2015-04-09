package org.jailbreak.service.core;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.TeamsFilters;

import com.google.common.base.Optional;

public interface TeamsManager {
	
	public Optional<Team> getRawTeam(int id);
	public Optional<Team> getTeam(int id);
	public Optional<Team> getTeamSlug(String slug);
	public Optional<Team> getLimitedTeam(int id);
	
	public List<Team> getAllTeams();
	public List<Team> getTeams(int limit);
	public List<Team> getTeams(int limit, TeamsFilters filters);
	public List<Team> getTeamsByLastCheckin();
	public HashMap<Integer, Team> getLimitedTeams(Set<Integer> ids);
	
	public Team addTeam(Team team);
	public Optional<Team> updateTeam(Team team);
	public Optional<Team> patchTeam(Team team);
	public void deleteTeam(int id);
	
	public int updateAllTeamPositions(int teamIdCausedUpdate);
	
}
