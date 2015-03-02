package org.jailbreak.service.core;

import java.util.HashMap;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Donate;

import com.google.common.base.Optional;

public interface DonateEventsManager {
	
	Optional<Donate> getDonateEvent(int id);
	HashMap<Integer, Donate> getDonateEvents(Set<Integer> ids);

}
