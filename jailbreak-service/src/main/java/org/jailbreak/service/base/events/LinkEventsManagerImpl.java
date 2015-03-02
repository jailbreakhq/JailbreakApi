package org.jailbreak.service.base.events;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Link;
import org.jailbreak.service.core.events.LinkEventsManager;
import org.jailbreak.service.db.dao.events.LinkEventsDAO;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.newrelic.deps.com.google.common.collect.Maps;

public class LinkEventsManagerImpl implements LinkEventsManager {

	private final LinkEventsDAO dao;
	
	@Inject
	public LinkEventsManagerImpl(LinkEventsDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public Optional<Link> getLinkEvent(int id) {
		return dao.getLink(id);
	}

	@Override
	public HashMap<Integer, Link> getLinkEvents(Set<Integer> ids) {
		if (ids.isEmpty()) {
			return Maps.newHashMapWithExpectedSize(0);
		}
		
		HashMap<Integer, Link> map = Maps.newHashMap();
		List<Link> links = dao.getLinks(ids);
		for(Link link : links) {
			map.put(link.getId(), link);
		}
		return map;
	}

}
