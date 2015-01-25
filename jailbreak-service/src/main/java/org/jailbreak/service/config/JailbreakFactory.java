package org.jailbreak.service.config;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JailbreakFactory {
	
    @NotNull
    @JsonProperty
    private long startTime;
    
    @NotNull
    @JsonProperty
    private double finalLocationLat;
    
    @NotNull
    @JsonProperty
    private double finalLocationLon;
    
    public long getStartTime() {
    	return startTime;
    }
    
    public double getFinalLocationLat() {
    	return finalLocationLon;
    }
    
    public double getFinalLocationLon() {
    	return finalLocationLon;
    }
    
}
