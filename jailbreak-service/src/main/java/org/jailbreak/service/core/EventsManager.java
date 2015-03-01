package org.jailbreak.service.core;

import java.util.List;

import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Event.EventsFilters;

public interface EventsManager {
	
	public List<Event> getEvents(int limit, EventsFilters filters);

}
