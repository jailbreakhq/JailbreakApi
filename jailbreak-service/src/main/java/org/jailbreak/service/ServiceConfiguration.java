package org.jailbreak.service;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;

import org.jailbreak.service.config.JailbreakFactory;
import org.jailbreak.service.config.ResourcesFactory;
import org.jailbreak.service.config.SentryFactory;
import org.jailbreak.service.config.StripeFactory;

public class ServiceConfiguration extends Configuration {
	
    // Database Settings
    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();
	
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
	
    // HTTP Client Settings
    @JsonProperty("httpClient")
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
	
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }

    public void setJerseyClientConfiguration(JerseyClientConfiguration config) {
        this.httpClient = config;
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
    
    // View Renderer
    @NotNull
    private ImmutableMap<String, ImmutableMap<String, String>> viewRendererConfiguration = ImmutableMap.of();

    @JsonProperty("viewRendererConfiguration")
    public ImmutableMap<String, ImmutableMap<String, String>> getViewRendererConfiguration() {
        return viewRendererConfiguration;
    }

    @JsonProperty("viewRendererConfiguration")
    public void setViewRendererConfiguration(Map<String, Map<String, String>> viewRendererConfiguration) {
        ImmutableMap.Builder<String, ImmutableMap<String, String>> builder = ImmutableMap.builder();
        for (Map.Entry<String, Map<String, String>> entry : viewRendererConfiguration.entrySet()) {
            builder.put(entry.getKey(), ImmutableMap.copyOf(entry.getValue()));
        }
        this.viewRendererConfiguration = builder.build();
    }
    
}