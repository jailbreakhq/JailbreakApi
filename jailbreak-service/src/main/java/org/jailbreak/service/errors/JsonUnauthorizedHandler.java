package org.jailbreak.service.errors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.dropwizard.auth.UnauthorizedHandler;

public class JsonUnauthorizedHandler implements UnauthorizedHandler {
    private static final String CHALLENGE_FORMAT = "%s realm=\"%s\"";

    @Override
    public Response buildResponse(String prefix, String realm) {
        // Create an HTTP 401 Unauthorized response with a JSON payload of a
        // human readable error
    	String message = "Credentials are required to access this resource.";
    	ErrorMessage errorObject = new ErrorMessage(401, message, ApiDocs.AUTHENTICATION);
    	
        return Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE,
                    String.format(CHALLENGE_FORMAT, prefix, realm))
                .type(MediaType.APPLICATION_JSON)
                .entity(errorObject)
                .build();
    }
}