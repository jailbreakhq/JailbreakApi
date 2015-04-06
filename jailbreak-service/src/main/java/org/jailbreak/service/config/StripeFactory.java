package org.jailbreak.service.config;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StripeFactory {
	
	@NotNull
    @JsonProperty
    private String secretKey;

	public String getSecretKey() {
		return secretKey;
	}
	
}
