package org.jailbreak.service.errors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage {
	
	@JsonProperty
	private int statusCode;
	
	@JsonProperty
	private String message;
	
	@JsonProperty
	private String docsLink;
	
	public ErrorMessage(int status_code, String message, String docs_link) {
		this.statusCode = status_code;
		this.message = message;
		this.docsLink = docs_link;
	}

}
