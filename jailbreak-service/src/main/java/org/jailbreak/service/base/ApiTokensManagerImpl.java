package org.jailbreak.service.base;

import org.jailbreak.api.representations.Representations.ApiToken;
import org.jailbreak.api.representations.Representations.FacebookAuthToken;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.client.FacebookClient;
import org.jailbreak.service.core.ApiTokensManager;
import org.jailbreak.service.core.UsersManager;
import org.jailbreak.service.db.ApiTokensDAO;
import org.jailbreak.service.errors.auth.ApiTokenExpiredException;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class ApiTokensManagerImpl implements ApiTokensManager {
	
	private final SecureTokenGeneratorImpl tokenGenerator;
	private final ApiTokensDAO dao;
	private final FacebookClient facebookClient;
	private final UsersManager usersManager;
	
	@Inject
	ApiTokensManagerImpl(SecureTokenGeneratorImpl tokenGenerator,
			ApiTokensDAO dao,
			FacebookClient facebookClient,
			UsersManager usersManager) {
		this.tokenGenerator = tokenGenerator;
		this.dao = dao;
		this.facebookClient = facebookClient;
		this.usersManager = usersManager;
	}
	
	@Override
	public ApiToken createNewToken(long user_id) {
		ApiToken token = ApiToken.newBuilder()
				.setApiToken(tokenGenerator.nextToken())
				.setUserId(user_id)
				.setExpires((System.currentTimeMillis()/1000L) + (18 * 60 * 60)) // 18 hours
				.build();
		this.dao.createApiToken(token);
		return token;
	}
	
	@Override
	public Optional<ApiToken> authenticateFacebookAccessToken(FacebookAuthToken access_token) {
		// check the user_id and access_token against FB
		Optional<User> fb_user = facebookClient.authenticateWithFacebook(access_token);
		
		if (!fb_user.isPresent()) {
			return Optional.absent();
		}

		// if this is a new user register them
		Optional<User> db_user = usersManager.getUser(fb_user.get().getUserId());
		if (!db_user.isPresent()) {
			usersManager.createUser(fb_user.get());
		}
		
		return Optional.of(createNewToken(fb_user.get().getUserId()));
	}
	
	@Override
	public Optional<User> authenticate(ApiToken api_token) throws ApiTokenExpiredException {
		Optional<ApiToken> maybe_api_token = dao.getApiToken(api_token.getUserId(), api_token.getApiToken());
		if (!maybe_api_token.isPresent()) {
			return Optional.absent();
		}
		
		ApiToken result = maybe_api_token.get();
		
		// check the token is still valid
		if (result.getExpires() < (System.currentTimeMillis() / 1000L)) {
			throw new ApiTokenExpiredException();
		}
		
		return usersManager.getUser(result.getUserId());
	}
	
}
