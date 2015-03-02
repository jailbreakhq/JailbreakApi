package org.jailbreak.service.base.events;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Twitter;
import org.jailbreak.service.core.events.TwitterEventsManager;
import org.jailbreak.service.db.dao.events.TwitterEventsDAO;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.newrelic.deps.com.google.common.collect.Maps;

public class TwitterEventsManagerImpl implements TwitterEventsManager {
	
	private final TwitterEventsDAO dao;
	
	@Inject
	public TwitterEventsManagerImpl(TwitterEventsDAO dao) {
		this.dao = dao;
	}

	@Override
	public Optional<Twitter> getTwitterEvent(int id) {
		return dao.getTwitterEvent(id);
	}

	@Override
	public HashMap<Integer, Twitter> getTwitterEvents(Set<Integer> ids) {
		HashMap<Integer, Twitter> map = Maps.newHashMap();
		List<Twitter> Twitters = dao.getTwitterEvents(ids);
		for(Twitter Twitter : Twitters) {
			map.put(Twitter.getId(), Twitter);
		}
		return map;
	}

}
