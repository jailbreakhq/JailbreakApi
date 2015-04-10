package org.jailbreak.service.resources;

import java.io.IOException;
import java.net.URLDecoder;

import javax.ws.rs.core.UriInfo;

import org.jailbreak.service.ServiceConfiguration;
import org.jailbreak.service.errors.BadRequestException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;

public class ResourcesHelper {
	
 	private final ServiceConfiguration config;
	
	@Inject
	public ResourcesHelper(ServiceConfiguration config) {
		this.config = config;
	}
	
	public String buildUrl(UriInfo uriInfo, String path) {
		String host = uriInfo.getBaseUri().toString();
		
		if (host.endsWith("/")) {
		    host = host.substring(0, host.length() - 1);
		}
		
		return host + path;
	}
	
	public String buildUrl(UriInfo uriInfo, String path, int id) {
		return buildUrl(uriInfo, path) + "/" + id;
	}
	
	public int limit(Optional<Integer> maybeLimit) {
		int defaultLimit = config.getResourcesSettings().getDefaultLimit();
		int maxLimit = config.getResourcesSettings().getMaxLimit();
		
		return this.limit(maybeLimit, defaultLimit, maxLimit);
	}
	
	public int eventsLimit(Optional<Integer> maybeLimit) {
		int defaultLimit = config.getResourcesSettings().getEventsDefaultLimit();
		int maxLimit = config.getResourcesSettings().getEventsMaxLimit();
		
		return this.limit(maybeLimit, defaultLimit, maxLimit);
	}
	
	public int limit(Optional<Integer> maybeLimit, int defaultLimit, int maxLimit) {
		Integer limit = defaultLimit;
		if (maybeLimit.isPresent()) {
			limit = maybeLimit.get();
			if (limit > maxLimit)
				limit = defaultLimit;
		}
		return limit;
	}
	
	public <T> T decodeUrlEncodedJson(Optional<String> maybeData, Class<T> klass, T nullObject, String docs_link) throws BadRequestException {
		if (maybeData.isPresent()) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new ProtobufModule());
			try {
				return mapper.readValue(URLDecoder.decode(maybeData.get(), "UTF-8"), klass);
			} catch (IllegalArgumentException | IOException e) {
				throw new BadRequestException("Filters were malformed. Could not parse JSON content in query param", docs_link);
			}
		} else {
			return nullObject;
		}
	}

}
