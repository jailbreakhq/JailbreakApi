package org.jailbreak.service.base;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Youtube;
import org.jailbreak.service.core.YoutubeEventsManager;
import org.jailbreak.service.db.dao.events.YoutubeEventsDAO;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.newrelic.deps.com.google.common.collect.Maps;

public class YoutubeEventsManagerImpl implements YoutubeEventsManager {
	
	private final YoutubeEventsDAO dao;
	
	@Inject
	public YoutubeEventsManagerImpl(YoutubeEventsDAO dao) {
		this.dao = dao;
	}

	@Override
	public Optional<Youtube> getYoutube(int id) {
		return dao.getYoutube(id);
	}

	@Override
	public HashMap<Integer, Youtube> getYoutubes(Set<Integer> ids) {
		HashMap<Integer, Youtube> map = Maps.newHashMap();
		List<Youtube> youtubes = dao.getYoutubes(ids);
		for(Youtube youtube : youtubes) {
			map.put(youtube.getId(), youtube);
		}
		return map;
	}

}
