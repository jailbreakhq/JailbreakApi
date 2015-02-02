package org.jailbreak.service.resources;

import java.io.IOException;
import java.net.URLDecoder;

import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.errors.BadRequestException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;

public class ResourcesHelper {
	
	public static int limit(Optional<Integer> maybeLimit, int defaultLimit, int maxLimit) {
		Integer limit = defaultLimit;
		if (maybeLimit.isPresent()) {
			limit = maybeLimit.get();
			if (limit > maxLimit)
				limit = defaultLimit;
		}
		return limit;
	}
	
	public static <T> T decodeUrlEncodedJson(String data, Class<T> klass) throws BadRequestException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new ProtobufModule());
		try {
			return mapper.readValue(URLDecoder.decode(data, "UTF-8"), klass);
		} catch (IllegalArgumentException | IOException e) {
			throw new BadRequestException("Donations filters were malformed. Could not parse JSON content in query param", ApiDocs.DONATIONS_FILTERS);
		}
	}

}
