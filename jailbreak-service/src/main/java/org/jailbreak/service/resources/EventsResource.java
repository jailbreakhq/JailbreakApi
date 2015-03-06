package org.jailbreak.service.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Event.EventsFilters;
import org.jailbreak.service.core.events.EventsManager;
import org.jailbreak.service.errors.ApiDocs;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@Path(Paths.EVENTS_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class EventsResource {
	
	private final EventsManager manager;
	private final int defaultLimit;
	private final int maxLimit;
	
	@Inject
	public EventsResource(EventsManager manager,
			@Named("resources.events.defaultLimit") int defaultLimit,
			@Named("resources.events.maxLimit") int maxLimit) {
		this.manager = manager;
		this.defaultLimit = defaultLimit;
		this.maxLimit = maxLimit;
	}
	
	@GET
	@Timed
	public List<Event> getEvents(@QueryParam("limit") Optional<Integer> maybeLimit,
			@QueryParam("filters") Optional<String> maybeFilters) {
		Integer limit = ResourcesHelper.limit(maybeLimit, defaultLimit, maxLimit);
		
		EventsFilters filters = ResourcesHelper.decodeUrlEncodedJson(maybeFilters, EventsFilters.class, EventsFilters.newBuilder().build(), ApiDocs.EVENTS_FILTERS);
		
		return manager.filterPrivateFields(manager.getEvents(limit, filters));
	}

}
