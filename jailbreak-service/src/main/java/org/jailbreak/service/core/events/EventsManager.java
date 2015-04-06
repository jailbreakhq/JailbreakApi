package org.jailbreak.service.core.events;

import java.util.List;

import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Event.EventsFilters;

import com.google.common.base.Optional;

public interface EventsManager {
	
	public Event createEvent(Event event);
	public boolean updateEvent(Event event);
	
	public Optional<Event> getRawEvent(int id);
	public Optional<Event> getEvent(int id);
	
	public List<Event> getEvents(int limit, EventsFilters filters);

}
