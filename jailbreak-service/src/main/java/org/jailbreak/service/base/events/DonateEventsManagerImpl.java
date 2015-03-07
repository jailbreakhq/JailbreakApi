package org.jailbreak.service.base.events;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Donate;
import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Event.EventType;
import org.jailbreak.service.core.events.DonateEventsManager;
import org.jailbreak.service.core.events.EventsManager;
import org.jailbreak.service.db.dao.events.DonateEventsDAO;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class DonateEventsManagerImpl implements DonateEventsManager {

	private final DonateEventsDAO dao;
	private final EventsManager eventsManager;
	
	@Inject
	public DonateEventsManagerImpl(DonateEventsDAO dao,
			EventsManager eventsManager) {
		this.dao = dao;
		this.eventsManager = eventsManager;
	}
	
	@Override
	public Optional<Donate> getDonateEvent(int id) {
		return dao.getDonateEvent(id);
	}

	@Override
	public HashMap<Integer, Donate> getDonateEvents(Set<Integer> ids) {
		if (ids.isEmpty()) {
			return Maps.newHashMapWithExpectedSize(0);
		}
		
		List<Donate> ds = dao.getDonateEvents(ids);
		HashMap<Integer, Donate> map = Maps.newHashMap();
		for (Donate d : ds) {
			map.put(d.getId(), d);
		}
		return map;
	}

	@Override
	public Donate createDonateEvent(Donate donate) {
		int newId = dao.insert(donate);
		
		Event.Builder event = Event.newBuilder()
				.setType(EventType.DONATE)
				.setObjectId(newId);
		
		if (donate.hasTeamId()) {
			event.setTeamId(donate.getTeamId());
		}
		
		eventsManager.createEvent(event.build());

		return getDonateEvent(newId).get(); 
	}
	
}
