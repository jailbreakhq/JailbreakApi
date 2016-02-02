package org.jailbreak.service.config;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourcesFactory {
	
	@NotNull
    @JsonProperty
    private int defaultLimit;
	
	@NotNull
    @JsonProperty
    private int maxLimit;
	
	@NotNull
    @JsonProperty
    private int eventsDefaultLimit;
	
	@NotNull
    @JsonProperty
    private int eventsMaxLimit;

	public int getDefaultLimit() {
		return defaultLimit;
	}

	public int getMaxLimit() {
		return maxLimit;
	}

	public int getEventsDefaultLimit() {
		return eventsDefaultLimit;
	}

	public int getEventsMaxLimit() {
		return eventsMaxLimit;
	}

}
