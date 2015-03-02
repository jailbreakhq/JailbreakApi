package org.jailbreak.service.base.events;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Donate;
import org.jailbreak.service.core.events.DonateEventsManager;
import org.jailbreak.service.db.dao.events.DonateEventsDAO;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class DonateEventsManagerImpl implements DonateEventsManager {

	private final DonateEventsDAO dao;
	
	@Inject
	public DonateEventsManagerImpl(DonateEventsDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public Optional<Donate> getDonateEvent(int id) {
		return dao.getDonateEvent(id);
	}

	@Override
	public HashMap<Integer, Donate> getDonateEvents(Set<Integer> ids) {
		List<Donate> ds = dao.getDonateEvents(ids);
		HashMap<Integer, Donate> map = Maps.newHashMap();
		for (Donate d : ds) {
			map.put(d.getId(), d);
		}
		return map;
	}

}
