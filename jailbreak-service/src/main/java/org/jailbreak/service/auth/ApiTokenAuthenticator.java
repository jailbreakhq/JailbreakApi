package org.jailbreak.service.auth;

import org.jailbreak.api.representations.Representations.ApiToken;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.service.core.ApiTokensManager;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class ApiTokenAuthenticator implements Authenticator<BasicCredentials, User> {
	
	private final ApiTokensManager apiTokenManager;
	
	@Inject
	public ApiTokenAuthenticator(ApiTokensManager apiTokenManager) {
		this.apiTokenManager = apiTokenManager;
	}
	
    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
    	Long userId = Long.parseLong(credentials.getUsername());
    	String token = credentials.getPassword();
    	
    	ApiToken apiToken = ApiToken.newBuilder()
    			.setUserId(userId)
    			.setApiToken(token)
    			.build();
    	
    	return this.apiTokenManager.authenticate(apiToken);
    }
}