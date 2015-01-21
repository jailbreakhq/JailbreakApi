package org.jailbreak.auth.authenticator;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import org.jailbreak.auth.client.AuthClient;
import org.jailbreak.auth.representations.Representations.User;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class JailbreakAuthenticator implements Authenticator<BasicCredentials, User> {
	
	private final AuthClient authClient;
	
	@Inject
	public JailbreakAuthenticator(AuthClient authClient) {
		this.authClient = authClient;
	}
	
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
    	Long user_id = Long.parseLong(credentials.getUsername());
    	String api_token = credentials.getPassword();
    	
    	return this.authClient.checkApiToken(user_id, api_token);
    }
}
