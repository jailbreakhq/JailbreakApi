package org.jailbreak.service;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

import org.jailbreak.client.FacebookClient;
import org.jailbreak.client.base.FacebookClientImpl;
import org.jailbreak.service.auth.ApiTokenAuthenticator;
import org.jailbreak.service.base.ApiTokensManagerImpl;
import org.jailbreak.service.base.CheckinsManagerImpl;
import org.jailbreak.service.base.DonationsManagerImpl;
import org.jailbreak.service.base.SecureTokenGeneratorImpl;
import org.jailbreak.service.base.TeamsManagerImpl;
import org.jailbreak.service.base.UsersManagerImpl;
import org.jailbreak.service.core.ApiTokensManager;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.core.DonationsManager;
import org.jailbreak.service.core.SecureTokenGenerator;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.core.UsersManager;
import org.jailbreak.service.db.ApiTokensDAO;
import org.jailbreak.service.db.CheckinsDAO;
import org.jailbreak.service.db.DonationsDAO;
import org.jailbreak.service.db.TeamsDAO;
import org.jailbreak.service.db.UsersDAO;
import org.skife.jdbi.v2.DBI;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class ServiceModule extends AbstractModule {
	
	private DBI dbi; // force this to be singleton (https://github.com/HubSpot/dropwizard-guice/issues/19)
	
	@Override
	protected void configure() {
		bind(CheckinsManager.class).to(CheckinsManagerImpl.class);
		bind(TeamsManager.class).to(TeamsManagerImpl.class);
		bind(DonationsManager.class).to(DonationsManagerImpl.class);
		bind(UsersManager.class).to(UsersManagerImpl.class);
		bind(ApiTokensManager.class).to(ApiTokensManagerImpl.class);
		bind(SecureTokenGenerator.class).to(SecureTokenGeneratorImpl.class);
		bind(FacebookClient.class).to(FacebookClientImpl.class);
		bind(Authenticator.class).to(ApiTokenAuthenticator.class);
	}
	
	@Provides
	@Named("jailbreak.startTime")
	public long provideStartTime(ServiceConfiguration config) {
		return config.getJailbreakSettings().getStartTime();
	}
	
	@Provides
	@Named("jailbreak.finalLocationLat")
	public double provideEndLat(ServiceConfiguration config) {
		return config.getJailbreakSettings().getFinalLocationLat();
	}
	
	@Provides
	@Named("jailbreak.finalLocationLon")
	public double provideEndLon(ServiceConfiguration config) {
		return config.getJailbreakSettings().getFinalLocationLon();
	}
	
	@Provides
	@Named("resources.maxLimit")
	public int provideResourcesMaxLimit(ServiceConfiguration config) {
		return config.getEnvironmentSettings().getMaxLimit();
	}
	
	@Provides
	@Named("resources.defaultLimit")
	public int provideResourcesDefaultLimit(ServiceConfiguration config) {
		return config.getEnvironmentSettings().getDefaultLimit();
	}
	
	@Provides
	@Named("donations.webhook.secret")
	public String provideDonationsWebhookSecret(ServiceConfiguration config) {
		return config.getEnvironmentSettings().getDonationsWebhookSecret();
	}
	
	@Provides
	private DBI getDatabaseConnection(ServiceConfiguration config, Environment env) throws ClassNotFoundException {
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
	
	@Provides
	public TeamsDAO provideTeamsDAO(DBI jdbi) {
        return jdbi.onDemand(TeamsDAO.class);
	}
	
	@Provides
	public CheckinsDAO provideCheckinsDAO(DBI jdbi) {
        return jdbi.onDemand(CheckinsDAO.class);
	}
	
	@Provides
	public DonationsDAO provideDonationsDAO(DBI jdbi) {
        return jdbi.onDemand(DonationsDAO.class);
	}

}
