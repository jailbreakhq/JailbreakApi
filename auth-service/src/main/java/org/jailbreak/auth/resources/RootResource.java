package org.jailbreak.auth.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jailbreak.auth.representations.Representations.AuthService;

@Path("/")
@Produces({MediaType.APPLICATION_JSON})
public class RootResource {
	
	@GET
	public AuthService getJailbreakInfo(@Context UriInfo uriInfo) {
		String path = uriInfo.getBaseUri().toString();
		
		// remove possible trailing slash
		if (path.endsWith("/")) {
		    path = path.substring(0, path.length() - 1);
		}
		
        return AuthService.newBuilder()
    		.setUsersUrl(path + Paths.USERS_PATH)
    		.setAuthenticateUrl(path + Paths.AUTHENTICATE_PATH)
    		.setFacebookTokensUrl(path + Paths.FACEBOOK_TOKENS_PATH)
    		.build();
	}

}