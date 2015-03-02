package org.jailbreak.service.core.events;

import java.util.HashMap;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Link;

import com.google.common.base.Optional;

public interface LinkEventsManager {
	
	Optional<Link> getLinkEvent(int id);
	HashMap<Integer, Link> getLinkEvents(Set<Integer> ids);

}
