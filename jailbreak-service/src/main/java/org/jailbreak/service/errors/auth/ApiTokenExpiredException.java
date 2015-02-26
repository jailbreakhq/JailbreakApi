package org.jailbreak.service.errors.auth;

import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.errors.BaseException;

@SuppressWarnings("serial")
public class ApiTokenExpiredException extends BaseException {

	private final static int CODE = 419;
	private final static String MESSAGE = "Your ApiToken has expired please re-authenticate to get a new ApiToken";
	private final static String LINK = ApiDocs.AUTHENTICATION;
	
	public ApiTokenExpiredException() {
		super(CODE, MESSAGE, LINK);
	}

}
