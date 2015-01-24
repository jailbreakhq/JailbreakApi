package org.jailbreak.service.base;

import java.util.List;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.db.CheckinsDAO;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class CheckinsManagerImpl implements CheckinsManager {

	private final CheckinsDAO dao;
	
	@Inject
	public CheckinsManagerImpl(CheckinsDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public Optional<Checkin> getCheckin(int team_id, int id) {
		return this.dao.getCheckin(team_id, id);
	}

	@Override
	public List<Checkin> getTeamCheckins(int team_id) {
		return this.dao.getTeamCheckins(team_id);
	}

}
