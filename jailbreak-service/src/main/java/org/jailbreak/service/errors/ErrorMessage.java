package org.jailbreak.service.errors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage {
	
	// don't change the case of properties as they are the API response field names
	
	@JsonProperty
	private int statusCode;
	
	@JsonProperty
	private String message;
	
	@JsonProperty
	private String docsLink;
	
	public ErrorMessage(int status, String message, String docs_link) {
		this.statusCode = status;
		this.message = message;
		this.docsLink = docs_link;
	}
	
}
