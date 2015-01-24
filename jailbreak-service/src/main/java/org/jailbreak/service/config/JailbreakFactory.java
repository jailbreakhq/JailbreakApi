package org.jailbreak.service.config;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JailbreakFactory {
	
    @NotNull
    @JsonProperty
    private long startTime;
    
    public long getStartTime() {
    	return startTime;
    }
    
}
