package org.jailbreak.service.resources;

import java.io.IOException;
import java.net.URLDecoder;

import javax.ws.rs.core.UriInfo;

import org.jailbreak.service.errors.BadRequestException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;

public class ResourcesHelper {
	
	public static String buildUrl(UriInfo uriInfo, String path) {
		String host = uriInfo.getBaseUri().toString();
		
		if (host.endsWith("/")) {
		    host = host.substring(0, host.length() - 1);
		}
		
		return host + path;
	}
	
	public static String buildUrl(UriInfo uriInfo, String path, int id) {
		return buildUrl(uriInfo, path) + "/" + id;
	}
	
	public static int limit(Optional<Integer> maybeLimit, int defaultLimit, int maxLimit) {
		Integer limit = defaultLimit;
		if (maybeLimit.isPresent()) {
			limit = maybeLimit.get();
			if (limit > maxLimit)
				limit = defaultLimit;
		}
		return limit;
	}
	
	public static <T> T decodeUrlEncodedJson(Optional<String> maybeData, Class<T> klass, T nullObject, String docs_link) throws BadRequestException {
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
