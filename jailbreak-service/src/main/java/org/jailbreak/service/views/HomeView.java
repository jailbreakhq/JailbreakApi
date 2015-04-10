package org.jailbreak.service.views;

import io.dropwizard.views.View;

import java.util.List;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.JailbreakService;
import org.jailbreak.api.representations.Representations.Team;

public class HomeView extends View {

	private final JailbreakService settings;
	private final List<Team> teams;
	private final List<Event> events;
	private final List<Donation> donations;
	private final int donationsCount;
	
	public HomeView(
			JailbreakService settings,
			List<Team> teams,
			List<Event> events,
			List<Donation> donations,
			int donationsCount) {
		super("home.ftl");
		this.settings = settings;
		this.teams = teams;
		this.events = events;
		this.donations = donations;
		this.donationsCount = donationsCount;
	}
	
	public JailbreakService getSettings() {
		return settings;
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
	
	public int getDonationsCount() {
		return donationsCount;
	}
	
}
