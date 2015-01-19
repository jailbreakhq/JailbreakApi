package org.jailbreak.auth.auth;

import org.jailbreak.auth.core.ApiTokensManager;
import org.jailbreak.auth.representations.Representations.ApiToken;
import org.jailbreak.auth.representations.Representations.User;

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
    	Long user_id = Long.parseLong(credentials.getUsername());
    	String token = credentials.getPassword();
    	
    	ApiToken apiToken = ApiToken.newBuilder()
    			.setUserId(user_id)
    			.setApiToken(token)
    			.build();
    	
    	return this.apiTokenManager.authenticate(apiToken);
    }
}