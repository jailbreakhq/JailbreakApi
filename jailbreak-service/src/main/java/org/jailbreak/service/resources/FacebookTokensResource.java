package org.jailbreak.service.resources;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jailbreak.api.representations.Representations.ApiToken;
import org.jailbreak.api.representations.Representations.FacebookAuthToken;
import org.jailbreak.service.core.ApiTokensManager;
import org.jailbreak.service.errors.auth.AuthenticationFailedException;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path(Paths.FACEBOOK_TOKENS_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FacebookTokensResource {

	private final ApiTokensManager manager;
	
	@Inject
	public FacebookTokensResource(ApiTokensManager manager) {
		this.manager = manager;
	}
	
	@POST
	public ApiToken authenticateFacebookAccessToken(@Valid FacebookAuthToken access_token) {
		Optional<ApiToken> token = this.manager.authenticateFacebookAccessToken(access_token);
		if (token.isPresent()) {
        	return token.get();
        } else {
        	throw new AuthenticationFailedException();
        }
	}
	
}