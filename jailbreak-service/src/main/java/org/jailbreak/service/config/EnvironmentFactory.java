package org.jailbreak.service.config;

public class EnvironmentFactory {
	
	private final int DEAFULT_DEFAULT_LIMIT = 10;
	private final int DEFAULT_MAX_LIMIT = 20;
	private final int DEFAULT_EVENTS_MAX_LIMIT = 50;
	
    public int getDefaultLimit() {
    	String envValue = System.getenv("DEFAULT_LIMIT");
    	if (envValue == null)
    		return DEAFULT_DEFAULT_LIMIT;
    	else
    		return Integer.parseInt(envValue);
    }
    
    public int getMaxLimit() {
    	String envValue = System.getenv("MAX_LIMIT");
    	if (envValue == null)
    		return DEFAULT_MAX_LIMIT;
    	else
    		return Integer.parseInt(envValue);
    }
    
    public int getEventsDefaultLimit() {
    	String envValue = System.getenv("EVENTS_DEFAULT_LIMIT");
    	if (envValue == null)
    		return DEAFULT_DEFAULT_LIMIT;
    	else
    		return Integer.parseInt(envValue);
    }
    
    public int getEventsMaxLimit() {
    	String envValue = System.getenv("EVENTS_MAX_LIMIT");
    	if (envValue == null)
    		return DEFAULT_EVENTS_MAX_LIMIT;
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
