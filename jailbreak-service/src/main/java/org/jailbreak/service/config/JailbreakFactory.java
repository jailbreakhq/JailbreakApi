package org.jailbreak.service.config;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JailbreakFactory {
	
    @NotNull
    @JsonProperty
    private long startTime;
    
    @NotNull
    @JsonProperty
    private long endTime;
	
    public long getStartTime() {
    	return startTime;
    }
    
    public long getEndTime() {
    	return endTime;
    }

}
