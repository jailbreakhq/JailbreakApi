package org.jailbreak.client;

import org.jailbreak.api.representations.Representations.FacebookAuthToken;
import org.jailbreak.api.representations.Representations.User;

import com.google.common.base.Optional;

public interface FacebookClient {
	
	public Optional<User> authenticateWithFacebook(FacebookAuthToken token); 

}