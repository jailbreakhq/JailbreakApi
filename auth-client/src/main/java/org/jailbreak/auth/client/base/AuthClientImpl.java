package org.jailbreak.auth.client.base;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.jailbreak.auth.client.AuthClient;
import org.jailbreak.auth.representations.Representations.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class AuthClientImpl implements AuthClient {
	
	private final Logger LOG = LoggerFactory.getLogger(AuthClientImpl.class);
	
	private final String api_host;
	private final Client httpClient;
	
	@Inject
	public AuthClientImpl(Client httpClient, @Named("api_host") String host) {
		this.api_host = host;
		this.httpClient = httpClient;
	}

	public Optional<User> checkApiToken(long user_id, String api_token) {
		WebResource webResource = httpClient.resource(api_host + "/authenticate");
 
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("user_id", Long.toString(user_id));
		queryParams.add("api_token", api_token);
		ClientResponse response = webResource.queryParams(queryParams)
											.accept(MediaType.APPLICATION_JSON)
											.get(ClientResponse.class);
 
		if (response.getStatus() != 200) {
		   LOG.info("Failed Authentication for user " + user_id);
		   return Optional.absent();
		}
		
		User user = response.getEntity(User.class);
		return Optional.of(user);
	}

}
