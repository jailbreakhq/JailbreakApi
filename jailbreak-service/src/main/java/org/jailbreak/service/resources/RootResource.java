package org.jailbreak.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jailbreak.api.representations.Representations.JailbreakService;
import org.jailbreak.service.core.DonationsManager;

import com.google.inject.Inject;
import com.google.inject.name.Named;

@Path("/")
@Produces({MediaType.APPLICATION_JSON})
public class RootResource {
	
	@Context
	private UriInfo uriInfo;
	
	private final DonationsManager donationsManager;
	private final long startTime;
	private final double startLat;
	private final double startLon;
	private final double finalLat;
	private final double finalLon;
	
	@Inject
	public RootResource(DonationsManager donationsManager,
			@Named("jailbreak.startTime") long startTime,
			@Named("jailbreak.startLocationLat") double startLat,
			@Named("jailbreak.startLocationLon") double startLon,
			@Named("jailbreak.finalLocationLat") double finalLat,
			@Named("jailbreak.finalLocationLon") double finalLon) {
		this.donationsManager = donationsManager;
		this.startTime = startTime;
		this.startLat = startLat;
		this.startLon = startLon;
		this.finalLat = finalLat;
		this.finalLon = finalLon;
	}
	
	@GET
	public JailbreakService getJailbreakInfo() {
		String path = ResourcesHelper.buildUrl(uriInfo, "");
		
		int amountRaised = donationsManager.getTotalRaised();
		
        return JailbreakService.newBuilder()
        	.setAmountRaised(amountRaised)
    		.setStartTime(startTime)
    		.setStartLocationLat(startLat)
    		.setStartLocationLon(startLon)
    		.setFinalLocationLat(roundLocationPercision(finalLat))
    		.setFinalLocationLon(roundLocationPercision(finalLon))
        	.setTeamsUrl(path + Paths.TEAMS_PATH)
        	.setDonationsUrl(path + Paths.DONATIONS_PATH)
        	.setEventsUrl(path + Paths.EVENTS_PATH)
    		.setUsersUrl(path + Paths.USERS_PATH)
    		.setAuthenticateUrl(path + Paths.AUTHENTICATE_PATH)
    		.build();
	}
	
	private double roundLocationPercision(double location) {
		return (double)Math.round(location * 1000000) / 1000000;
	}

}