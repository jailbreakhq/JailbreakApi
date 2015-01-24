package org.jailbreak.service.errors.auth;

import org.jailbreak.service.errors.AppException;

@SuppressWarnings("serial")
public class AuthenticationFailedException extends AppException {
	
	private final static int CODE = 401;
	private final static String MESSAGE = "Authentication Failed. Your user id and password combination are invalid";
	private final static String LINK = ApiDocs.AUTHENTICATION;
	
	public AuthenticationFailedException() {
		super(CODE, MESSAGE, LINK);
	}
	
}