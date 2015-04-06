package org.jailbreak.service.config;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JailbreakFactory {
	
    @NotNull
    @JsonProperty
    private long startTime;
    
    @NotNull
    @JsonProperty
    private double startLat;
    
    @NotNull
    @JsonProperty
    private double startLon;
    
    @NotNull
    @JsonProperty
    private double finalLat;
    
    @NotNull
    @JsonProperty
    private double finalLon;
    
    public long getStartTime() {
    	return startTime;
    }

	public double getStartLat() {
		return startLat;
	}

	public double getStartLon() {
		return startLon;
	}

	public double getFinalLat() {
		return finalLat;
	}

	public double getFinalLon() {
		return finalLon;
	}
    
}
