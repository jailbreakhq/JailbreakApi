package org.jailbreak.service.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jailbreak.api.representations.Representations.ApiToken;
import org.jailbreak.api.representations.Representations.AuthRequest;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.service.core.ApiTokensManager;
import org.jailbreak.service.errors.auth.AuthenticationFailedException;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path(Paths.AUTHENTICATE_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {
	
	private final ApiTokensManager tokensManager;
	
	@Inject
	public AuthenticationResource(ApiTokensManager tokens) {
		this.tokensManager = tokens;
	}
	
	@POST
	
	public User authenticateApiToken(ApiToken api_token) {
		Optional<User> user;
		user = tokensManager.authenticate(api_token);
		if (user.isPresent()) {
        	return user.get();
        } else {
        	throw new AuthenticationFailedException();
        }
	}
	
	
	@Timed
	@POST
	@Path(Paths.LOGIN_PATH)
	public ApiToken login(AuthRequest request) {
		AuthRequest cleaned = AuthRequest.newBuilder()
				.setEmail(request.getEmail().trim().toLowerCase())
				.setPassword(request.getPassword().trim())
				.build();
				
		Optional<ApiToken> token = tokensManager.authenticate(cleaned);
		if (token.isPresent()) {
			return token.get();
		} else {
			throw new AuthenticationFailedException();
		}
	}
	
}

