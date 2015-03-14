package org.jailbreak.service.errors;

import org.jailbreak.service.errors.BaseException;

@SuppressWarnings("serial")
public class ServiceTemporarilyUnavailable extends BaseException {

	private final static int CODE = 503;
	
	public ServiceTemporarilyUnavailable(String message, String link) {
		super(CODE, message, link);
	}
	
}
