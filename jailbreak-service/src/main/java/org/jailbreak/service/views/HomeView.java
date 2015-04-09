package org.jailbreak.service.views;

import io.dropwizard.views.View;

import java.util.List;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Team;

public class HomeView extends View {

	private final List<Team> teams;
	private final List<Event> events;
	private final List<Donation> donations;
	
	public HomeView(List<Team> teams, List<Event> events, List<Donation> donations) {
		super("home.ftl");
		this.teams = teams;
		this.events = events;
		this.donations = donations;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public List<Event> getEvents() {
		return events;
	}

	public List<Donation> getDonations() {
		return donations;
	}
	
}
