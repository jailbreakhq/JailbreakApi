package org.jailbreak.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import javax.ws.rs.client.Client;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import net.kencochrane.raven.Raven;
import net.kencochrane.raven.RavenFactory;
import net.kencochrane.raven.dsn.Dsn;

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
import org.jailbreak.service.base.events.DonateEventsManagerImpl;
import org.jailbreak.service.base.events.EventsManagerImpl;
import org.jailbreak.service.base.events.FacebookEventsManagerImpl;
import org.jailbreak.service.base.events.InstagramEventsManagerImpl;
import org.jailbreak.service.base.events.LinkEventsManagerImpl;
import org.jailbreak.service.base.events.TwitterEventsManagerImpl;
import org.jailbreak.service.base.events.VineEventsManagerImpl;
import org.jailbreak.service.base.events.YoutubeEventsManagerImpl;
import org.jailbreak.service.core.ApiTokensManager;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.core.DonationsManager;
import org.jailbreak.service.core.SecureTokenGenerator;
import org.jailbreak.service.core.StripeManager;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.core.UsersManager;
import org.jailbreak.service.core.events.DonateEventsManager;
import org.jailbreak.service.core.events.EventsManager;
import org.jailbreak.service.core.events.FacebookEventsManager;
import org.jailbreak.service.core.events.InstagramEventsManager;
import org.jailbreak.service.core.events.LinkEventsManager;
import org.jailbreak.service.core.events.TwitterEventsManager;
import org.jailbreak.service.core.events.VineEventsManager;
import org.jailbreak.service.core.events.YoutubeEventsManager;
import org.jailbreak.service.db.dao.ApiTokensDAO;
import org.jailbreak.service.db.dao.CheckinsDAO;
import org.jailbreak.service.db.dao.DonationsDAO;
import org.jailbreak.service.db.dao.TeamsDAO;
import org.jailbreak.service.db.dao.UsersDAO;
import org.jailbreak.service.db.dao.events.DonateEventsDAO;
import org.jailbreak.service.db.dao.events.EventsDAO;
import org.jailbreak.service.db.dao.events.FacebookEventsDAO;
import org.jailbreak.service.db.dao.events.InstagramEventsDAO;
import org.jailbreak.service.db.dao.events.LinkEventsDAO;
import org.jailbreak.service.db.dao.events.TwitterEventsDAO;
import org.jailbreak.service.db.dao.events.VineEventsDAO;
import org.jailbreak.service.db.dao.events.YoutubeEventsDAO;
import org.jailbreak.service.helpers.DistanceHelper;
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
	private Client client;
	private Slugify slg;
	private Raven raven;
	
	@Override
	protected void configure() {
		bind(EventsManager.class).to(EventsManagerImpl.class);
		bind(LinkEventsManager.class).to(LinkEventsManagerImpl.class);
		bind(DonateEventsManager.class).to(DonateEventsManagerImpl.class);
		bind(TwitterEventsManager.class).to(TwitterEventsManagerImpl.class);
		bind(FacebookEventsManager.class).to(FacebookEventsManagerImpl.class);
		bind(InstagramEventsManager.class).to(InstagramEventsManagerImpl.class);
		bind(VineEventsManager.class).to(VineEventsManagerImpl.class);
		bind(YoutubeEventsManager.class).to(YoutubeEventsManagerImpl.class);
		
		bind(TeamsManager.class).to(TeamsManagerImpl.class);
		bind(CheckinsManager.class).to(CheckinsManagerImpl.class);
		bind(DistanceHelper.class);
		
		bind(DonationsManager.class).to(DonationsManagerImpl.class);
		bind(StripeManager.class).to(StripeManagerImpl.class);
		
		bind(UsersManager.class).to(UsersManagerImpl.class);
		bind(ApiTokensManager.class).to(ApiTokensManagerImpl.class);
		bind(SecureTokenGenerator.class).to(SecureTokenGeneratorImpl.class);
		bind(FacebookClient.class).to(FacebookClientImpl.class);
		bind(Authenticator.class).to(ApiTokenAuthenticator.class);
	}
	
	@Provides
	@Named("stripe.secret.key")
	public String provideStripeSecretKey(ServiceConfiguration config) {
		return config.getStripeSettings().getSecretKey();
	}
	
	@Provides
	public Raven provideRaven(ServiceConfiguration config) {
		if (raven == null) {
			if (config.getSentrySettings().getEnabled()) {
				String dsn = config.getSentrySettings().getDSN();
				raven = RavenFactory.ravenInstance(new Dsn(dsn));
			} else {
				LOG.info("Sentry is disabled");
			}
		}
		return raven;
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
	private Client getHttpClient(ServiceConfiguration config, Environment env) {
		if (this.client == null) {
			this.client = new JerseyClientBuilder(env).using(config.getJerseyClientConfiguration()).build("JailbreakApi");
		}
		return this.client;
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
	public EventsDAO provideEventsDAO(Connection conn) {
		EventsDAO dao = dbi.onDemand(EventsDAO.class);
        dao.conn = this.getJDBCHandler(null);
        return dao;
	}
	
	@Provides
	public YoutubeEventsDAO provideYoutubeEventsDAO(DBI jdbi) {
        return jdbi.onDemand(YoutubeEventsDAO.class);
	}
	
	@Provides
	public DonateEventsDAO provideDonateEventsDAO(DBI jdbi) {
        return jdbi.onDemand(DonateEventsDAO.class);
	}
	
	@Provides
	public LinkEventsDAO provideLinkEventsDAO(DBI jdbi) {
        return jdbi.onDemand(LinkEventsDAO.class);
	}
	
	@Provides
	public TwitterEventsDAO provideTwitterEventsDAO(DBI jdbi) {
        return jdbi.onDemand(TwitterEventsDAO.class);
	}
	
	@Provides
	public FacebookEventsDAO provideFacebookEventsDAO(DBI jdbi) {
        return jdbi.onDemand(FacebookEventsDAO.class);
	}
	
	@Provides
	public InstagramEventsDAO provideInstagramEventsDAO(DBI jdbi) {
        return jdbi.onDemand(InstagramEventsDAO.class);
	}
	
	@Provides
	public VineEventsDAO provideVineEventsDAO(DBI jdbi) {
        return jdbi.onDemand(VineEventsDAO.class);
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
