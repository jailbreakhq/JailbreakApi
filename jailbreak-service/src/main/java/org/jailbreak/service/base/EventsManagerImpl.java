package org.jailbreak.service.base;

import java.util.List;

import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Event.EventsFilters;
import org.jailbreak.service.core.EventsManager;

import com.google.common.collect.Lists;

public class EventsManagerImpl implements EventsManager {

	@Override
	public List<Event> getEvents(int limit, EventsFilters filters) {
		return Lists.newArrayList();
	}

}
