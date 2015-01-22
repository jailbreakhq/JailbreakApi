package org.jailbreak.common.errors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage {
	
	@JsonProperty
	private int status_code;
	
	@JsonProperty
	private String message;
	
	@JsonProperty
	private String docs_link;
	
	public ErrorMessage(int status_code, String message, String docs_link) {
		this.status_code = status_code;
		this.message = message;
		this.docs_link = docs_link;
	}

}
