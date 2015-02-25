package org.jailbreak.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

import org.jailbreak.client.FacebookClient;
import org.jailbreak.client.base.FacebookClientImpl;
import org.jailbreak.service.auth.ApiTokenAuthenticator;
import org.jailbreak.service.base.ApiTokensManagerImpl;
import org.jailbreak.service.base.CheckinsManagerImpl;
import org.jailbreak.service.base.DonationsManagerImpl;
import org.jailbreak.service.base.SecureTokenGeneratorImpl;
import org.jailbreak.service.base.StripeManagerImpl;
import org.jailbreak.service.base.TeamsManagerImpl;
import org.jailbreak.service.base.UsersManagerImpl;
import org.jailbreak.service.core.ApiTokensManager;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.core.DonationsManager;
import org.jailbreak.service.core.SecureTokenGenerator;
import org.jailbreak.service.core.StripeManager;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.core.UsersManager;
import org.jailbreak.service.db.ApiTokensDAO;
import org.jailbreak.service.db.CheckinsDAO;
import org.jailbreak.service.db.DonationsDAO;
import org.jailbreak.service.db.TeamsDAO;
import org.jailbreak.service.db.UsersDAO;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.slugify.Slugify;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class ServiceModule extends AbstractModule {
	
	private Logger LOG = LoggerFactory.getLogger(ServiceModule.class);
	private DBI dbi; // force this to be singleton (https://github.com/HubSpot/dropwizard-guice/issues/19)
	private Connection conn;
	private Slugify slg;
	
	@Override
	protected void configure() {
		bind(CheckinsManager.class).to(CheckinsManagerImpl.class);
		bind(TeamsManager.class).to(TeamsManagerImpl.class);
		bind(DonationsManager.class).to(DonationsManagerImpl.class);
		bind(StripeManager.class).to(StripeManagerImpl.class);
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
	@Named("jailbreak.startLocationLat")
	public double provideStartLat(ServiceConfiguration config) {
		return config.getJailbreakSettings().getStartLocationLat();
	}
	
	@Provides
	@Named("jailbreak.startLocationLon")
	public double provideStartLon(ServiceConfiguration config) {
		return config.getJailbreakSettings().getStartLocationLon();
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
	@Named("stripe.secret.key")
	public String providesStripeSecretKey(ServiceConfiguration config) {
		return config.getEnvironmentSettings().getStripeSecretKey();
	}
	
	@Provides
	private Connection getJDBCHandler(ServiceConfiguration config) {
		if (this.conn == null) {
			DataSourceFactory ds = config.getDataSourceFactory();
			try {
				this.conn = DriverManager.getConnection(ds.getUrl(), ds.getUser(), ds.getPassword());
			} catch (SQLException e) {
				LOG.error("Error getting JDBC connection handler setup");
			}
		}
		return this.conn;
	}
	
	@Provides
	private DBI getDatabaseConnection(ServiceConfiguration config, Environment env) throws ClassNotFoundException {
		if (this.dbi == null) {
			final DBIFactory factory = new DBIFactory();
			this.dbi = factory.build(env, config.getDataSourceFactory(), "postgres");
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
	public TeamsDAO provideTeamsDAO(Connection conn) {
		TeamsDAO dao = dbi.onDemand(TeamsDAO.class);
        dao.conn = this.getJDBCHandler(null);
        return dao;
	}
	
	@Provides
	public CheckinsDAO provideCheckinsDAO(DBI jdbi) {
        return jdbi.onDemand(CheckinsDAO.class);
	}
	
	@Provides
	public DonationsDAO provideDonationsDAO(Connection conn) {
        DonationsDAO dao = dbi.onDemand(DonationsDAO.class);
        dao.conn = this.getJDBCHandler(null);
        return dao;
	}
	
	@Provides
	public Slugify provideSlugify() {
		if (this.slg == null) {
			try {
				this.slg = new Slugify();
				HashMap<String, String> replacements = new HashMap<String, String>();
				replacements.put("&", "and");
				this.slg.setCustomReplacements(replacements);
			} catch (IOException e) {
				LOG.error("Slugify threw IOException: " + e.getMessage());
			}
		}
		return this.slg;
	}

}
