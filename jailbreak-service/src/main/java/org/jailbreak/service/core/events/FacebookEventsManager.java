package org.jailbreak.service.core.events;


import java.util.HashMap;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Facebook;

import com.google.common.base.Optional;

public interface FacebookEventsManager {

	public Optional<Facebook> getFacebookEvent(int id);
	public HashMap<Integer, Facebook> getFacebookEvents(Set<Integer> ids);
	
}
