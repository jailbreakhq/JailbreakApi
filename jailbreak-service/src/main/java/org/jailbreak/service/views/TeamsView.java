package org.jailbreak.service.views;

import io.dropwizard.views.View;

import java.util.List;

import org.jailbreak.api.representations.Representations.Team;

public class TeamsView extends View {
	
	private final List<Team> teams;
	private final int teamsCount;
	
	public TeamsView(
			List<Team> teams,
			int teamsCount) {
		super("teams.ftl");
		this.teams = teams;
		this.teamsCount = teamsCount;
	}
	
	public List<Team> getTeams() {
		return teams;
	}

	public int getTeamsCount() {
		return teamsCount;
	}

}
