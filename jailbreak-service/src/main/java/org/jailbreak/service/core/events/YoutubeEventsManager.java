package org.jailbreak.service.core.events;


import java.util.HashMap;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Youtube;

import com.google.common.base.Optional;

public interface YoutubeEventsManager {

	public Optional<Youtube> getYoutubeEvent(int id);
	public HashMap<Integer, Youtube> getYoutubeEvents(Set<Integer> ids);
	
}
