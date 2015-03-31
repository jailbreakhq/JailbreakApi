package org.jailbreak.service.errors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage {
	
	@JsonProperty
	private int status;
	
	@JsonProperty
	private String message;
	
	@JsonProperty
	private String docs_link;
	
	public ErrorMessage(int status, String message, String docs_link) {
		this.status = status;
		this.message = message;
		this.docs_link = docs_link;
	}
	
}
