package org.jailbreak.common.errors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
	
	private final Logger LOG = LoggerFactory.getLogger(GenericExceptionMapper.class);

	public Response toResponse(Throwable ex) {
		String message;
		int status_code = getHttpStatus(ex);
		
		if(status_code == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			// we messed up - hit the details from the user
			message = "Internal Server Error";
			
		} else {
			// they messed up - give them as much information as possible to solve their issue
			message = ex.getMessage();
		}
		
		ErrorMessage errorObject = new ErrorMessage(status_code, message, "");
		
		LOG.debug(ex.getMessage());
		
		return Response.status(this.getHttpStatus(ex))
				.entity(errorObject)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	private int getHttpStatus(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException)ex).getResponse().getStatus();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(); // internal server error 500
        }
    }

}
