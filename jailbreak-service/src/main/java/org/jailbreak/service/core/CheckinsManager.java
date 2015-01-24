package org.jailbreak.service.core;

import java.util.List;

import org.jailbreak.api.representations.Representations.Checkin;

import com.google.common.base.Optional;

public interface CheckinsManager {
	
	Checkin createCheckin(Checkin checkin);
	Optional<Checkin> updateCheckin(int id, Checkin checkin);
	Optional<Checkin> getCheckin(int id);
	Optional<Checkin> getTeamCheckin(int team_id, int id);
	List<Checkin> getTeamCheckins(int team_id);

}
