package org.jailbreak.auth;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthConfiguration extends Configuration {
	
    // Database Settings
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();
	
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }
    
}