package org.jailbreak.auth;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthConfiguration extends Configuration {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthConfiguration.class);
	
    // Database Settings
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();
	
    
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
    	LOG.info("Dropwizard dummy DB URL will be overridden by environment variable DATABASE_URL");
        database = HerokuDatabaseConfiguration.create();
        return database;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }
    
}