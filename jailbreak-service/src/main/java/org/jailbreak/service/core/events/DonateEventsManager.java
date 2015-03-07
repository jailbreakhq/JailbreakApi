package org.jailbreak.service.core.events;

import java.util.HashMap;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Donate;

import com.google.common.base.Optional;

public interface DonateEventsManager {
	
	public Optional<Donate> getDonateEvent(int id);
	public HashMap<Integer, Donate> getDonateEvents(Set<Integer> ids);
	public Donate createDonateEvent(Donate donate);
	
}
