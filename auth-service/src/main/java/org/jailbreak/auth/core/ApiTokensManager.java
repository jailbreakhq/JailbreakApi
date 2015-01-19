package org.jailbreak.auth.core;

import org.jailbreak.auth.errors.ApiTokenExpiredException;
import org.jailbreak.auth.representations.Representations.ApiToken;
import org.jailbreak.auth.representations.Representations.FacebookAuthToken;
import org.jailbreak.auth.representations.Representations.User;

import com.google.common.base.Optional;

public interface ApiTokensManager {

	public ApiToken createNewToken(long user_id);
	public Optional<ApiToken> authenticateFacebookAccessToken(FacebookAuthToken access_token);
	public Optional<User> authenticate(ApiToken api_token) throws ApiTokenExpiredException;
	
}
