package org.jailbreak.client.base;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.jailbreak.client.FacebookClient;
import org.jailbreak.api.representations.Representations.FacebookAuthToken;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.api.representations.Representations.User.Gender;
import org.jailbreak.api.representations.Representations.User.UserLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class FacebookClientImpl implements FacebookClient {
	
	private final Logger LOG = LoggerFactory.getLogger(FacebookClientImpl.class);
	
	private final Client httpClient;
	
	@Inject
	public FacebookClientImpl(Client httpClient) {
		this.httpClient = httpClient;
	}

	@Timed
	@Override
	public Optional<User> authenticateWithFacebook(FacebookAuthToken token) {
		// make HTTP call to facebook /me endpoint
		WebResource webResource = httpClient.resource("https://graph.facebook.com/me");
		
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("user_id", Long.toString(token.getUserId()));
		queryParams.add("access_token", token.getAccessToken());
		ClientResponse response = webResource.queryParams(queryParams)
											.accept(MediaType.APPLICATION_JSON)
											.get(ClientResponse.class);
 
		if (response.getStatus() != 200) {
		   LOG.info("Failed Authenticating with Facebook: HTTP : " + response.getStatus());
		   return Optional.absent();
		}
 
		// parse json response
		String json = response.getEntity(String.class);
		LOG.debug(json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actual_obj;
		try {
			actual_obj = mapper.readTree(json);
		} catch (IOException e) {
			LOG.error("Failed to parse JSON response from Facebook. " + json);
			return Optional.absent();
		}
		
		// map JSON object into User
		String email = actual_obj.get("email").asText();
		String first_name = actual_obj.get("first_name").asText();
		String last_name = actual_obj.get("last_name").asText();
		String gender = actual_obj.get("gender").asText();
		String link = actual_obj.get("link").asText();
		String locale = actual_obj.get("locale").asText();
		int timezone = actual_obj.get("timezone").asInt();
		
		User.Builder builder = User.newBuilder()
				.setUserId(token.getUserId())
				.setEmail(email)
				.setFirstName(first_name)
				.setLastName(last_name)
				.setFacebookLink(link)
				.setLocale(locale)
				.setTimezone(timezone)
				.setUserLevel(UserLevel.NORMAL);
		
		if (gender.equals("male")) {
			builder.setGender(Gender.MALE);
		} else if (gender.equalsIgnoreCase("female")) {
			builder.setGender(Gender.FEMALE);
		} else {
			builder.setGender(Gender.OTHER);
		}
		
		return Optional.of(builder.build());
	}

}