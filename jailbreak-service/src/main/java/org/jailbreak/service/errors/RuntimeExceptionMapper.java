package org.jailbreak.service.errors;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.kencochrane.raven.Raven;
import net.kencochrane.raven.event.Event;
import net.kencochrane.raven.event.EventBuilder;
import net.kencochrane.raven.event.interfaces.ExceptionInterface;

public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
	
	private final Raven raven;
	private static final Logger LOG = LoggerFactory.getLogger(RuntimeExceptionMapper.class);
	
	public RuntimeExceptionMapper(Raven raven) {
		this.raven = raven;
	}
	
	@Override
    public Response toResponse(RuntimeException e) {
		Exception report = null;
		String reportMessage = "";
		
		int status;
        String message;
        String docsLink = "";
		
        // Build Response
		if (e instanceof AppException) {
			status = 500;
			message = "Internal Server Error";
			report = ((AppException) e).getOriginalException();
			reportMessage = e.getMessage() + " -- " + report.getMessage();
			
		} else if (e instanceof BaseException) {
			BaseException base = (BaseException)e;
			status = base.getStatus();
			message = base.getMessage();
			docsLink = base.getDocsLink();
			report = e;
			reportMessage = e.getMessage();
			
		} else if (e instanceof WebApplicationException) {
			WebApplicationException webException = (WebApplicationException) e;
			Response resp = webException.getResponse();
			status = resp.getStatus();
			
			if (status == 404) {
				message = "Object not found";
			} else if (status == 405) {
				message = "HTTP Method not supported";
			} else {
				message = e.getMessage();
			}
			
		} else {
			status = 500;
			message = "Internal Server Error";
			report = e;
			reportMessage = e.getMessage();
		}
		
		// Report the error details to Sentry
		if (raven != null && status == 500) {
			Event event = new EventBuilder()
				.withTimestamp(new Date())
				.withLevel(Event.Level.ERROR)
				.withMessage(reportMessage)
				.withSentryInterface(new ExceptionInterface(report))
				.build();
			
			raven.sendEvent(event);
		}
		
        // Log the error
 		StringWriter stackTrace = new StringWriter();
 		e.printStackTrace(new PrintWriter(stackTrace));
 		
 		LOG.error(stackTrace.toString());
        
 		// Return appropriate JSON response message
        ErrorMessage errorObject = new ErrorMessage(status, message, docsLink);
        
        return Response.status(status)
        		.entity(errorObject)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
