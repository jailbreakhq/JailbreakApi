package org.jailbreak.service.core;

import java.util.List;

import org.jailbreak.api.representations.Representations.Checkin;

import com.google.common.base.Optional;

public interface CheckinsManager {
	
	Checkin createCheckin(Checkin checkin);
	Optional<Checkin> updateCheckin(Checkin checkin);
	
	Optional<Checkin> getCheckin(int id);
	List<Checkin> getCheckins(List<Integer> ids);
	
	Optional<Checkin> getTeamCheckin(int teamId, int id);
	Optional<Checkin> getLastTeamCheckin(int teamId);
	List<Checkin> getTeamCheckins(int teamId);

}
