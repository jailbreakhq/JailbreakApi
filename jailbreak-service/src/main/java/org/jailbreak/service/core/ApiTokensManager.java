package org.jailbreak.service.core;

import org.jailbreak.api.representations.Representations.ApiToken;
import org.jailbreak.api.representations.Representations.AuthRequest;
import org.jailbreak.api.representations.Representations.FacebookAuthToken;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.service.errors.auth.ApiTokenExpiredException;

import com.google.common.base.Optional;

public interface ApiTokensManager {

	public ApiToken createNewToken(long user_id);
	public Optional<ApiToken> authenticateFacebookAccessToken(FacebookAuthToken access_token);
	public Optional<User> authenticate(ApiToken api_token) throws ApiTokenExpiredException;
	public Optional<ApiToken> authenticate(AuthRequest request);
	
}
