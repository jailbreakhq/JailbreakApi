package org.jailbreak.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jailbreak.api.representations.Representations.JailbreakService;
import org.jailbreak.service.ServiceConfiguration;
import org.jailbreak.service.core.DonationsManager;

import com.google.inject.Inject;

@Path("/")
@Produces({MediaType.APPLICATION_JSON})
public class RootResource {
	
	@Context
	private UriInfo uriInfo;
	
	private final DonationsManager donationsManager;
	private final ServiceConfiguration config;;
	
	@Inject
	public RootResource(DonationsManager donationsManager,
			ServiceConfiguration config) {
		this.donationsManager = donationsManager;
		this.config = config;
	}
	
	@GET
	public JailbreakService getJailbreakInfo() {
		String path = ResourcesHelper.buildUrl(uriInfo, "");
		
		int amountRaised = donationsManager.getTotalRaised();
		
        return JailbreakService.newBuilder()
        	.setAmountRaised(amountRaised)
    		.setStartTime(config.getJailbreakSettings().getStartTime())
    		.setStartLocationLat(config.getJailbreakSettings().getStartLat())
    		.setStartLocationLon(config.getJailbreakSettings().getStartLon())
    		.setFinalLocationLat(config.getJailbreakSettings().getFinalLat())
    		.setFinalLocationLon(config.getJailbreakSettings().getFinalLon())
        	.setTeamsUrl(path + Paths.TEAMS_PATH)
        	.setDonationsUrl(path + Paths.DONATIONS_PATH)
        	.setEventsUrl(path + Paths.EVENTS_PATH)
    		.setUsersUrl(path + Paths.USERS_PATH)
    		.setAuthenticateUrl(path + Paths.AUTHENTICATE_PATH)
    		.build();
	}
	
}