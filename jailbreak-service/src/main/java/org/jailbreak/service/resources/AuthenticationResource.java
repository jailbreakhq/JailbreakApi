package org.jailbreak.service.resources;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jailbreak.api.representations.Representations.ApiToken;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.auth.errors.AuthenticationFailedException;
import org.jailbreak.service.core.ApiTokensManager;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path(Paths.AUTHENTICATE_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {
	
	private final ApiTokensManager manager;
	
	@Inject
	public AuthenticationResource(ApiTokensManager manager) {
		this.manager = manager;
	}
	
	@POST
	public User authenticateApiToken(@Valid ApiToken api_token) {
		Optional<User> user;
		user = this.manager.authenticate(api_token);
		if (user.isPresent()) {
        	return user.get();
        } else {
        	throw new AuthenticationFailedException();
        }
	}
	
}

