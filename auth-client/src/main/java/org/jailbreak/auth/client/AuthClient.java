package org.jailbreak.auth.client;

import org.jailbreak.auth.representations.Representations.User;

import com.google.common.base.Optional;

public interface AuthClient {
	
	Optional<User> checkApiToken(long user_id, String api_token);

}
