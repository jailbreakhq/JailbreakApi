package org.jailbreak.service.base.events;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Vine;
import org.jailbreak.service.core.events.VineEventsManager;
import org.jailbreak.service.db.dao.events.VineEventsDAO;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.newrelic.deps.com.google.common.collect.Maps;

public class VineEventsManagerImpl implements VineEventsManager {
	
	private final VineEventsDAO dao;
	
	@Inject
	public VineEventsManagerImpl(VineEventsDAO dao) {
		this.dao = dao;
	}

	@Override
	public Optional<Vine> getVineEvent(int id) {
		return dao.getVine(id);
	}

	@Override
	public HashMap<Integer, Vine> getVineEvents(Set<Integer> ids) {
		if (ids.isEmpty()) {
			return Maps.newHashMapWithExpectedSize(0);
		}
		
		HashMap<Integer, Vine> map = Maps.newHashMap();
		List<Vine> Vines = dao.getVines(ids);
		for(Vine Vine : Vines) {
			map.put(Vine.getId(), Vine);
		}
		return map;
	}

}
