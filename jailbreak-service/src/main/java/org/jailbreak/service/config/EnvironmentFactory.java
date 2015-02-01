package org.jailbreak.service.config;

public class EnvironmentFactory {
	
	private final int DEFAULT_LIMIT = 10;
	private final int MAX_LIMIT = 20;
	
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
    
    public String getDonationsWebhookSecret() {
    	String envValue = System.getenv("DONATIONS_WEBHOOK_SECRET");
    	if (envValue == null) {
    		throw new RuntimeException("DONATIONS_WEBHOOK_SECRET environment variable is not set. It is a required environment.");
    	}
    	
    	return envValue;
    }

}
