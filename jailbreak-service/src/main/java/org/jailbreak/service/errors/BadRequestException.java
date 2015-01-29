package org.jailbreak.service.errors;

@SuppressWarnings("serial")
public class BadRequestException extends AppException {

	private final static int CODE = 400;
	
	public BadRequestException(String message, String link) {
		super(CODE, message, link);
	}
	
}
