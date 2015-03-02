package org.jailbreak.service.core;


import java.util.HashMap;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Youtube;

import com.google.common.base.Optional;

public interface YoutubeEventsManager {

	public Optional<Youtube> getYoutube(int id);
	public HashMap<Integer, Youtube> getYoutubes(Set<Integer> ids);
	
}
