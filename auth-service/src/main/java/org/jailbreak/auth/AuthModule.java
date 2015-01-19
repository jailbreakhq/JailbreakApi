package org.jailbreak.auth;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

import org.jailbreak.auth.auth.ApiTokenAuthenticator;
import org.jailbreak.auth.base.ApiTokensManagerImpl;
import org.jailbreak.auth.base.SecureTokenGeneratorImpl;
import org.jailbreak.auth.base.UsersManagerImpl;
import org.jailbreak.auth.client.FacebookClient;
import org.jailbreak.auth.client.base.FacebookClientImpl;
import org.jailbreak.auth.core.ApiTokensManager;
import org.jailbreak.auth.core.SecureTokenGenerator;
import org.jailbreak.auth.core.UsersManager;
import org.jailbreak.auth.db.ApiTokensDAO;
import org.jailbreak.auth.db.UsersDAO;
import org.skife.jdbi.v2.DBI;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class AuthModule extends AbstractModule {
	
	private DBI dbi; // force this to be singleton (https://github.com/HubSpot/dropwizard-guice/issues/19)
	
	@Override
	protected void configure() {
		bind(UsersManager.class).to(UsersManagerImpl.class);
		bind(ApiTokensManager.class).to(ApiTokensManagerImpl.class);
		bind(SecureTokenGenerator.class).to(SecureTokenGeneratorImpl.class);
		bind(FacebookClient.class).to(FacebookClientImpl.class);
		bind(Authenticator.class).to(ApiTokenAuthenticator.class);
	}
	
	@Provides
	private DBI getDatabaseConnection(AuthConfiguration config, Environment env) throws ClassNotFoundException {
		if (this.dbi == null) {
			final DBIFactory factory = new DBIFactory();
			this.dbi = factory.build(env, config.getDataSourceFactory(), "mysql");
		}
        return this.dbi;
	}
	
	@Provides
	public UsersDAO provideUsersDAO(DBI jdbi) {
        return jdbi.onDemand(UsersDAO.class);
	}
	
	@Provides
	public ApiTokensDAO provideApiTokensDAO(DBI jdbi) {
        return jdbi.onDemand(ApiTokensDAO.class);
	}

}
