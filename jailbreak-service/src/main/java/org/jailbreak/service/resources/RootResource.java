package org.jailbreak.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jailbreak.api.representations.Representations.JailbreakService;

import com.google.inject.Inject;
import com.google.inject.name.Named;


@Path("/")
@Produces({MediaType.APPLICATION_JSON})
public class RootResource {
	
	private final long startTime;
	private final double startLat;
	private final double startLon;
	private final double finalLat;
	private final double finalLon;
	
	@Inject
	public RootResource(@Named("jailbreak.startTime") long startTime,
			@Named("jailbreak.startLocationLat") double startLat,
			@Named("jailbreak.startLocationLon") double startLon,
			@Named("jailbreak.finalLocationLat") double finalLat,
			@Named("jailbreak.finalLocationLon") double finalLon) {
		this.startTime = startTime;
		this.startLat = startLat;
		this.startLon = startLon;
		this.finalLat = finalLat;
		this.finalLon = finalLon;
	}
	
	@GET
	public JailbreakService getJailbreakInfo(@Context UriInfo uriInfo) {
		String path = uriInfo.getBaseUri().toString();
		
		// remove possible trailing slash
		if (path.endsWith("/")) {
		    path = path.substring(0, path.length() - 1);
		}
		
        return JailbreakService.newBuilder()
    		.setStartTime(startTime)
    		.setStartLocationLat(startLat)
    		.setStartLocationLon(startLon)
    		.setFinalLocationLat(finalLat)
    		.setFinalLocationLon(finalLon)
        	.setTeamsUrl(path + Paths.TEAMS_PATH)
    		.setUsersUrl(path + Paths.USERS_PATH)
    		.setAuthenticateUrl(path + Paths.AUTHENTICATE_PATH)
    		.setFacebookTokensUrl(path + Paths.FACEBOOK_TOKENS_PATH)
    		.build();
	}

}