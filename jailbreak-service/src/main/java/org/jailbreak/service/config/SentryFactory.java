package org.jailbreak.service.config;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SentryFactory {
	
	@NotNull
    @JsonProperty
    private boolean enabled;
	
	@NotNull
    @JsonProperty
    private String dsn;

	public boolean getEnabled() {
		return enabled;
	}

	public String getDSN() {
		return dsn;
	}

}
