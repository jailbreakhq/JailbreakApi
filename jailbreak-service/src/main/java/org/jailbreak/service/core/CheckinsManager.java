package org.jailbreak.service.core;

import java.util.List;

import org.jailbreak.api.representations.Representations.Checkin;

import com.google.common.base.Optional;

public interface CheckinsManager {
	
	Optional<Checkin> getCheckin(int team_id, int id);
	List<Checkin> getTeamCheckins(int team_id);

}
