package org.jailbreak.service.errors;

import org.jailbreak.service.errors.BaseException;

@SuppressWarnings("serial")
public class BadRequestException extends BaseException {

	private final static int CODE = 400;
	
	public BadRequestException(String message, String link) {
		super(CODE, message, link);
	}
	
}
