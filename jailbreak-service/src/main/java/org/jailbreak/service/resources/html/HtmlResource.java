package org.jailbreak.service.resources.html;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.JailbreakService;
import org.jailbreak.api.representations.Representations.Donation.DonationsFilters;
import org.jailbreak.api.representations.Representations.Event.EventsFilters;
import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.TeamsFilters;
import org.jailbreak.service.ServiceConfiguration;
import org.jailbreak.service.core.DonationsManager;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.core.events.EventsManager;
import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.resources.ResourcesHelper;
import org.jailbreak.service.views.HomeView;
import org.jailbreak.service.views.TeamsView;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/html")
@Produces(MediaType.TEXT_HTML)
public class HtmlResource {
	
	@Context
	private UriInfo uriInfo;
	
	private final TeamsManager teamsManager;
	private final EventsManager eventsManager;
	private final DonationsManager donationsManager;
	private final ServiceConfiguration config;
	private final ResourcesHelper helper;
	
	@Inject
	public HtmlResource(TeamsManager teamsManager,
			EventsManager eventsManager,
			DonationsManager donationsManager,
			ServiceConfiguration config,
			ResourcesHelper helper) {
		this.teamsManager = teamsManager;
		this.eventsManager = eventsManager;
		this.donationsManager = donationsManager;
		this.config = config;
		this.helper = helper;
	}
	
	@Path("/home")
	@GET
	@Timed
	public HomeView getHomeView() {
		int amountRaised = donationsManager.getTotalRaised();
		
		// build only the necessary attributes of the JailbreakService object
		// that are needed for the HomeView
		JailbreakService settings = JailbreakService.newBuilder()
	        	.setAmountRaised(amountRaised)
	    		.setStartTime(config.getJailbreakSettings().getStartTime())
	    		.setStartLocationLat(config.getJailbreakSettings().getStartLat())
	    		.setStartLocationLon(config.getJailbreakSettings().getStartLon())
	    		.setFinalLocationLat(config.getJailbreakSettings().getFinalLat())
	    		.setFinalLocationLon(config.getJailbreakSettings().getFinalLon())
	    		.build();
		
		List<Team> teams = teamsManager.getAllTeams();
		List<Event> events = eventsManager.getEvents(20, EventsFilters.getDefaultInstance());
		List<Donation> donations = donationsManager.getDonations(10);
		int donationsCount = donationsManager.getTotalCount(DonationsFilters.getDefaultInstance());
		return new HomeView(settings, teams, events, donations, donationsCount);
	}
	
	@Path("/teams")
	@GET
	public TeamsView getTeamsView(@QueryParam("page") Optional<Integer> page,
			@QueryParam("filters") Optional<String> maybeFilters) {
		TeamsFilters filters = helper.decodeUrlEncodedJson(maybeFilters, TeamsFilters.class, TeamsFilters.newBuilder().build(), ApiDocs.TEAMS_FILTERS);
		
		List<Team> teams = teamsManager.getTeams(20, page, filters);
		int count = teamsManager.getTotalCount(filters);
		
		return new TeamsView(teams, count);
	}
	
}