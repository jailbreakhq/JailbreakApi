package org.jailbreak.errors.common;

@SuppressWarnings("serial")
public class ForbiddenException extends AppException {

	private final static int CODE = 403;
	
	public ForbiddenException(String message, String link) {
		super(CODE, message, link);
	}
	
}