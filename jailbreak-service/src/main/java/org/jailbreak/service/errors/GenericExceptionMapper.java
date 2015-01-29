package org.jailbreak.service.errors;

import java.io.PrintWriter;
import java.io.StringWriter;

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
			
		} else if (status_code == Response.Status.NOT_FOUND.getStatusCode()) {
			message = "Object not found";
			
		} else {
			// they messed up - give them as much information as possible to solve their issue
			message = ex.getMessage();
		}
		
		// Log the error
		StringWriter stackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(stackTrace));
		
		LOG.error(ex.toString());
		LOG.error(stackTrace.toString());
		
		
		ErrorMessage errorObject = new ErrorMessage(status_code, message, "");

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
