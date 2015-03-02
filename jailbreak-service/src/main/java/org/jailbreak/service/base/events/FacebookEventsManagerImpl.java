package org.jailbreak.service.base.events;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Facebook;
import org.jailbreak.service.core.events.FacebookEventsManager;
import org.jailbreak.service.db.dao.events.FacebookEventsDAO;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.newrelic.deps.com.google.common.collect.Maps;

public class FacebookEventsManagerImpl implements FacebookEventsManager {
	
	private final FacebookEventsDAO dao;
	
	@Inject
	public FacebookEventsManagerImpl(FacebookEventsDAO dao) {
		this.dao = dao;
	}

	@Override
	public Optional<Facebook> getFacebookEvent(int id) {
		return dao.getFacebookEvent(id);
	}

	@Override
	public HashMap<Integer, Facebook> getFacebookEvents(Set<Integer> ids) {
		if (ids.isEmpty()) {
			return Maps.newHashMapWithExpectedSize(0);
		}
		
		HashMap<Integer, Facebook> map = Maps.newHashMap();
		List<Facebook> Facebooks = dao.getFacebookEvents(ids);
		for(Facebook Facebook : Facebooks) {
			map.put(Facebook.getId(), Facebook);
		}
		return map;
	}

}
