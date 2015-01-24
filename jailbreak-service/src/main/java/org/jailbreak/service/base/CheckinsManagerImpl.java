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
	public Checkin createCheckin(Checkin checkin) {
		int new_id = this.dao.insert(checkin);
		return this.getCheckin(new_id).get();
	}
	
	@Override
	public Optional<Checkin> updateCheckin(int id, Checkin checkin) {
		int result = dao.update(id, checkin);
		if (result == 0) {
			return Optional.of(checkin);
		} else {
			return Optional.absent();
		}
	}
	
	@Override
	public Optional<Checkin> getCheckin(int id) {
		return this.dao.getCheckin(id);
	}
	
	@Override
	public Optional<Checkin> getTeamCheckin(int team_id, int id) {
		return this.dao.getTeamCheckin(team_id, id);
	}

	@Override
	public List<Checkin> getTeamCheckins(int team_id) {
		return this.dao.getTeamCheckins(team_id);
	}

}
