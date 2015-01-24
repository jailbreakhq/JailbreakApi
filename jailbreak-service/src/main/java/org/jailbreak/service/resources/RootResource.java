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
	
	@Inject
	public RootResource(@Named("jailbreak.startTime") long startTime) {
		this.startTime = startTime;
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
        	.setTeamsUrl(path + Paths.TEAMS_PATH)
    		.setUsersUrl(path + Paths.USERS_PATH)
    		.setAuthenticateUrl(path + Paths.AUTHENTICATE_PATH)
    		.setFacebookTokensUrl(path + Paths.FACEBOOK_TOKENS_PATH)
    		.build();
	}

}