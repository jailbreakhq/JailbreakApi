package org.jailbreak.service.core.events;


import java.util.HashMap;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Instagram;

import com.google.common.base.Optional;

public interface InstagramEventsManager {

	public Optional<Instagram> getInstagramEvent(int id);
	public HashMap<Integer, Instagram> getInstagramEvents(Set<Integer> ids);
	
}
