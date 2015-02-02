package org.jailbreak.service.config;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JailbreakFactory {
	
    @NotNull
    @JsonProperty
    private long startTime;
    
    @NotNull
    @JsonProperty
    private double startLocationLat;
    
    @NotNull
    @JsonProperty
    private double startLocationLon;
    
    @NotNull
    @JsonProperty
    private double finalLocationLat;
    
    @NotNull
    @JsonProperty
    private double finalLocationLon;
    
    public long getStartTime() {
    	return startTime;
    }
    
    public double getStartLocationLat() {
    	return startLocationLat;
    }
    
    public double getStartLocationLon() {
    	return startLocationLon;
    }
    
    public double getFinalLocationLat() {
    	return finalLocationLat;
    }
    
    public double getFinalLocationLon() {
    	return finalLocationLon;
    }
    
}
