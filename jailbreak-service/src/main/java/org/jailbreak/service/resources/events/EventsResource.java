package org.jailbreak.service.resources.events;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Event.EventsFilters;
import org.jailbreak.service.core.events.EventsManager;
import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.resources.Paths;
import org.jailbreak.service.resources.ResourcesHelper;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.newrelic.deps.com.google.common.collect.Lists;

@Path(Paths.EVENTS_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class EventsResource {
	
	@Context
	private UriInfo uriInfo;
	
	private final EventsManager manager;
	private final ResourcesHelper helper;
	
	@Inject
	public EventsResource(EventsManager manager,
			ResourcesHelper helper) {
		this.manager = manager;
		this.helper = helper;
	}
	
	@GET
	@Timed
	public List<Event> getEvents(@QueryParam("limit") Optional<Integer> maybeLimit,
			@QueryParam("filters") Optional<String> maybeFilters) {
		Integer limit = helper.eventsLimit(maybeLimit);
		EventsFilters filters = helper.decodeUrlEncodedJson(maybeFilters, EventsFilters.class, EventsFilters.getDefaultInstance(), ApiDocs.EVENTS_FILTERS);
		
		return response(manager.getEvents(limit, filters));
	}
	
	@GET
	@Path("/{id}")
	public Optional<Event> getEvent(@PathParam("id") int id) {
		return response(manager.getEvent(id));
	}
	
	// response builder
	private List<Event> response(List<Event> events) {
		List<Event> results = Lists.newArrayListWithCapacity(events.size());
		for (Event event : events) {
			Event.Builder builder = event.toBuilder();
			builder.clearObjectId();
			builder.setHref(helper.buildUrl(uriInfo, Paths.EVENTS_PATH, event.getId()));
			results.add(builder.build());
		}
		return results;
	}
	
	private Optional<Event> response(Optional<Event> maybe) {
		if (maybe.isPresent()) {
			return Optional.of(response(Lists.newArrayList(maybe.get())).get(0));
		} else {
			return Optional.absent();
		}
	}

}
