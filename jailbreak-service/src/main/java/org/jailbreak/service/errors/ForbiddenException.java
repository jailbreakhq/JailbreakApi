package org.jailbreak.service.errors;

@SuppressWarnings("serial")
public class ForbiddenException extends BaseException {

	private final static int CODE = 403;
	
	public ForbiddenException(String message, String link) {
		super(CODE, message, link);
	}
	
}
