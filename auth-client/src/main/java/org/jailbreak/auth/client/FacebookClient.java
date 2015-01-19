package org.jailbreak.auth.client;

import org.jailbreak.auth.representations.Representations.FacebookAuthToken;
import org.jailbreak.auth.representations.Representations.User;

import com.google.common.base.Optional;

public interface FacebookClient {
	
	public Optional<User> authenticateWithFacebook(FacebookAuthToken token); 

}
