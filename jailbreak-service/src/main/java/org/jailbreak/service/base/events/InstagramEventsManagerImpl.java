package org.jailbreak.service.base.events;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Instagram;
import org.jailbreak.service.core.events.InstagramEventsManager;
import org.jailbreak.service.db.dao.events.InstagramEventsDAO;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.newrelic.deps.com.google.common.collect.Maps;

public class InstagramEventsManagerImpl implements InstagramEventsManager {
	
	private final InstagramEventsDAO dao;
	
	@Inject
	public InstagramEventsManagerImpl(InstagramEventsDAO dao) {
		this.dao = dao;
	}

	@Override
	public Optional<Instagram> getInstagramEvent(int id) {
		return dao.getInstagram(id);
	}

	@Override
	public HashMap<Integer, Instagram> getInstagramEvents(Set<Integer> ids) {
		if (ids.isEmpty()) {
			return Maps.newHashMapWithExpectedSize(0);
		}
		
		HashMap<Integer, Instagram> map = Maps.newHashMap();
		List<Instagram> Instagrams = dao.getInstagrams(ids);
		for(Instagram Instagram : Instagrams) {
			map.put(Instagram.getId(), Instagram);
		}
		return map;
	}

}
