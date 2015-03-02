package org.jailbreak.service.core.events;


import java.util.HashMap;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Twitter;

import com.google.common.base.Optional;

public interface TwitterEventsManager {

	public Optional<Twitter> getTwitterEvent(int id);
	public HashMap<Integer, Twitter> getTwitterEvents(Set<Integer> ids);
	
}
