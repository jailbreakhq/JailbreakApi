package org.jailbreak.service.config;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class EnvironmentFactory {
	
	private final int DEFAULT_DEFAULT_LIMIT = 10;
	private final int DEFAULT_MAX_LIMIT = 20;
	private final int DEFAULT_EVENTS_MAX_LIMIT = 50;
	
	private final Logger LOG = LoggerFactory.getLogger(EnvironmentFactory.class);
	
	public float getFinalLocationLat() {
		return getMandatoryFloat("FINAL_LAT");
	}
	
	public float getFinalLocationLon() {
		return getMandatoryFloat("FINAL_LON");
	}
	
    public int getDefaultLimit() {
    	return getIntWithDefault("DEFAULT_LIMIT", DEFAULT_DEFAULT_LIMIT);
    }
    
    public int getMaxLimit() {
    	return getIntWithDefault("MAX_LIMIT", DEFAULT_MAX_LIMIT);
    }
    
    public int getEventsDefaultLimit() {
    	return getIntWithDefault("EVENTS_DEFAULT_LIMIT", DEFAULT_DEFAULT_LIMIT);
    }
    
    public int getEventsMaxLimit() {
    	return getIntWithDefault("EVENTS_MAX_LIMIT", DEFAULT_EVENTS_MAX_LIMIT);
    }
    
    public String getStripeSecretKey() {
    	return getMandatoryString("STRIPE_SECRET_KEY");
    }
    
    public String getSentryDSN() {
    	String envValue = System.getenv("SENTRY_DSN");
    	if (envValue == null && this.getSentryEnabled()) {
    		throw new RuntimeException("SENTRY_DSN environment variable is not set. It is a requried environment varialbe when env var SENTRY_ENABLED is true.");
    	}
    	return envValue;
    }
    
    public boolean getSentryEnabled() {
    	boolean value = getBooleanWithDefault("SENTRY_ENABLED", true);
    	LOG.info("Sentry Enabled: " + value);
    	return value;
    }
    
	public void requestAllManadtory() {
		// TODO hook startup event from here instead of requiring a call to this function in ServiceApplication
		// this method will trigger runtime exceptions if the ENV variables are empty
		// it is called at start-up time so that we discover these problems early
		this.getStripeSecretKey();
		this.getSentryDSN();
		this.getFinalLocationLat();
		this.getFinalLocationLon();
	}
	
	private float getMandatoryFloat(String envvar) {
		String envValue = System.getenv(envvar);
    	if (envValue == null)
    		throw new RuntimeException(envvar + " environment variable is not set.");
    	else
    		return Float.parseFloat(envValue); 
	}
	
	private String getMandatoryString(String envvar) {
		return getMandatoryString(envvar, envvar + " environment variable is not set. It is a required environment variable");
	}
	
	private String getMandatoryString(String envvar, String reason) {
		String envValue = System.getenv(envvar);
    	if (envValue == null)
    		throw new RuntimeException(reason);
    	else
    		return envValue;
		
	}
	
	private int getIntWithDefault(String envvar, int defaultValue) {
		String envValue = System.getenv(envvar);
    	if (envValue == null)
    		return defaultValue;
    	else
    		return Integer.parseInt(envValue);
	}
	
	private boolean getBooleanWithDefault(String envvar, boolean defaultValue) {
		String envValue = System.getenv(envvar);
		if (envValue == null) {
			return defaultValue;
		} else {
			return Boolean.parseBoolean(envValue);
		}
	}
	
}
