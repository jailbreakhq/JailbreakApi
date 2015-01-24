package org.jailbreak.errors.common;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class AppException extends WebApplicationException {
	
	public AppException(int status_code, String message, String docs_link) {
		super(Response.status(status_code)
				.entity(new ErrorMessage(status_code, message, docs_link))
				.type(MediaType.APPLICATION_JSON)
				.build());
	}

}
