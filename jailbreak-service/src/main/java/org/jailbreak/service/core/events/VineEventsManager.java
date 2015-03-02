package org.jailbreak.service.core.events;


import java.util.HashMap;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Vine;

import com.google.common.base.Optional;

public interface VineEventsManager {

	public Optional<Vine> getVineEvent(int id);
	public HashMap<Integer, Vine> getVineEvents(Set<Integer> ids);
	
}
