package org.jailbreak.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.jailbreak.service.config.JailbreakFactory;
import org.jailbreak.service.config.ResourcesFactory;
import org.jailbreak.service.config.SentryFactory;
import org.jailbreak.service.config.StripeFactory;

public class ServiceConfiguration extends Configuration {
	
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
    
    // Resources  Settings
 	@Valid
 	@NotNull
 	private ResourcesFactory resourceSettings = new ResourcesFactory();
 	
	@JsonProperty("resources")
	public ResourcesFactory getResourcesSettings() {
		return this.resourceSettings;
	}
	 
	@JsonProperty("resources")
	public void setResourcesSettings(ResourcesFactory factory) {
		this.resourceSettings = factory;
	}
    
    // Sentry Error Reporting Settings
    @Valid
	@NotNull
	private SentryFactory sentrySettings = new SentryFactory();
	
    @JsonProperty("sentry")
    public SentryFactory getSentrySettings() {
    	return this.sentrySettings;
    }
    
    @JsonProperty("sentry")
    public void setSentrySettings(SentryFactory factory) {
    	this.sentrySettings = factory;
    }
    
    // Stripe API Settings
    @Valid
	@NotNull
	private StripeFactory stripeSettings = new StripeFactory();
	
    @JsonProperty("stripe")
    public StripeFactory getStripeSettings() {
    	return this.stripeSettings;
    }
    
    @JsonProperty("stripe")
    public void setStripeSettings(StripeFactory factory) {
    	this.stripeSettings = factory;
    }
	
    // Database Settings
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();
	
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }
    
    // HTTP Client Settings
    @JsonProperty
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
	
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }

    public void setJerseyClientConfiguration(JerseyClientConfiguration config) {
        this.httpClient = config;
    }
    
}