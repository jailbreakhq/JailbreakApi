package org.jailbreak.service.base;

import java.sql.SQLException;
import java.util.List;

import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Event.EventsFilters;
import org.jailbreak.service.core.EventsManager;
import org.jailbreak.service.db.EventsDAO;
import org.jailbreak.service.errors.AppException;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class EventsManagerImpl implements EventsManager {

	private final EventsDAO dao;
	
	@Inject
	public EventsManagerImpl(EventsDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public List<Event> getEvents(int limit, EventsFilters filters) {
		try {
			return dao.getFilteredEvents(limit, filters);
		} catch (SQLException e) {
			throw new AppException("Database error retrieving events", e);
		}
	}
	
	@Override
	public List<Event> filterPrivateFields(List<Event> events) {
		List<Event> filtered = Lists.newArrayListWithCapacity(events.size());
		for (Event event : events) {
			Event.Builder builder = event.toBuilder();
			
			builder.clearObjectId();
			
			filtered.add(builder.build());
		}
		return filtered;
	}

}
