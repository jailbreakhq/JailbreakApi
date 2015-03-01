package org.jailbreak.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvironmentFactory {
	
	private final int DEFAULT_LIMIT = 10;
	private final int MAX_LIMIT = 20;
	private final Logger LOG = LoggerFactory.getLogger(EnvironmentFactory.class);
	
    public int getDefaultLimit() {
    	String envValue = System.getenv("DEFAULT_LIMIT");
    	if (envValue == null)
    		return DEFAULT_LIMIT;
    	else
    		return Integer.parseInt(envValue);
    }
    
    public int getMaxLimit() {
    	String envValue = System.getenv("MAX_LIMIT");
    	if (envValue == null)
    		return MAX_LIMIT;
    	else
    		return Integer.parseInt(envValue);
    }
    
    public String getStripeSecretKey() {
    	String envValue = System.getenv("STRIPE_SECRET_KEY");
    	if (envValue == null) {
    		throw new RuntimeException("STRIPE_SECRET_KEY environment variable is not set. It is a requried environment varialbe.");
    	}
    	return envValue;
    }
    
    public String getSentryDSN() {
    	String envValue = System.getenv("SENTRY_DSN");
    	if (envValue == null && this.getSentryEnabled()) {
    		throw new RuntimeException("SENTRY_DSN environment variable is not set. It is a requried environment varialbe when env var SENTRY_ENABLED is true.");
    	}
    	return envValue;
    }
    
    public boolean getSentryEnabled() {
    	boolean result;
    	
    	String envValue = System.getenv("SENTRY_ENABLED");
    	if (envValue == null) {
    		result = true;
    	} else {
    		result = Boolean.parseBoolean(envValue);
    	}
    	return result;
    }
    
	public void requestAllManadtory() {
		// this method will trigger runtime exceptions if the ENV variables are empty
		// it is called at start-up time so that we discover these problems early
		this.getStripeSecretKey();
		this.getSentryDSN();
	}

}
