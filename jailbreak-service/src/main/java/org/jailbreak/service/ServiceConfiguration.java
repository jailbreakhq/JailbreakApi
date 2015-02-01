package org.jailbreak.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.jailbreak.service.config.EnvironmentFactory;
import org.jailbreak.service.config.HerokuDatabaseConfiguration;
import org.jailbreak.service.config.JailbreakFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceConfiguration extends Configuration {
	
	private static final Logger LOG = LoggerFactory.getLogger(ServiceConfiguration.class);
	
	// Environment Variables Settings
	@NotNull
	private EnvironmentFactory environmentSettings = new EnvironmentFactory();
	
	public EnvironmentFactory getEnvironmentSettings() {
		return environmentSettings;
	}
	
	// Jailbreak Competition Settings
	@Valid
	@NotNull
	private JailbreakFactory jailbreakSettings = new JailbreakFactory();
	
    @JsonProperty("jailbreak")
    public JailbreakFactory getJailbreakSettings() {
    	return this.jailbreakSettings;
    }
    
    @JsonProperty("jailbreak")
    public void setJailbreakSettings(JailbreakFactory factory) {
    	this.jailbreakSettings = factory;
    }
	
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