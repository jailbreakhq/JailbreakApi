package org.jailbreak.service.errors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class BaseException extends WebApplicationException {
	
	public BaseException(int status_code, String message, String docs_link) {
		super(Response.status(status_code)
				.entity(new ErrorMessage(status_code, message, docs_link))
				.type(MediaType.APPLICATION_JSON)
				.build());
	}

}
