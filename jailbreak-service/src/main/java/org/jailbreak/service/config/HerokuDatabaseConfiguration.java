package org.jailbreak.service.config;

import io.dropwizard.db.DataSourceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class that uses the DATABASE_URL env variable set by heroku instead of using the config
 */
public class HerokuDatabaseConfiguration {
    final static Logger LOG = LoggerFactory.getLogger(HerokuDatabaseConfiguration.class);
    private static DataSourceFactory databaseConfiguration;

    public static DataSourceFactory create() {
    	if (databaseConfiguration != null) {
    		return databaseConfiguration;
    	}
    	String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            throw new IllegalArgumentException("The DATABASE_URL environment variable must be set before running the app " +
                    "example: DATABASE_URL=\"postgres://username:password@host:5432/dbname\"");
        }
        DataSourceFactory databaseConfiguration = null;
        try {
            URI dbUri = new URI(databaseUrl);
            final String user = dbUri.getUserInfo().split(":")[0];
            final String password = dbUri.getUserInfo().split(":")[1];
            final String url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            LOG.info("Database URL: " + url);
            LOG.info("Database User: " + user);
            LOG.info("Database Password: *************");
            databaseConfiguration = new DataSourceFactory();
            databaseConfiguration.setUser(user);
            databaseConfiguration.setPassword(password);
            databaseConfiguration.setUrl(url);
            databaseConfiguration.setDriverClass("org.postgresql.Driver");
        } catch (URISyntaxException e) {
            LOG.info(e.getMessage());
        }
        return databaseConfiguration;
    }
}