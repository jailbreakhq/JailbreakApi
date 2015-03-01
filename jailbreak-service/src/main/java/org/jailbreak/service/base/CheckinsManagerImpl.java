package org.jailbreak.service.base;

import java.util.List;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.db.CheckinsDAO;
import org.jailbreak.service.helpers.DistanceHelper;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class CheckinsManagerImpl implements CheckinsManager {

	private final CheckinsDAO dao;
	private final DistanceHelper distanceHelper;
	
	@Inject
	public CheckinsManagerImpl(CheckinsDAO dao,
			DistanceHelper distanceHelper) {
		this.dao = dao;
		this.distanceHelper = distanceHelper;
	}
	
	@Override
	public Checkin createCheckin(Checkin checkin) {
		int new_id = this.dao.insert(checkin);
		return this.addDistanceToX(this.getCheckin(new_id).get());
	}
	
	@Override
	public Optional<Checkin> updateCheckin(Checkin checkin) {
		int result = dao.update(checkin);
		if (result == 0) {
			return Optional.of(checkin);
		} else {
			return Optional.absent();
		}
	}
	
	@Override
	public Optional<Checkin> getCheckin(int id) {
		return this.addDistanceToX(this.dao.getCheckin(id));
	}
	
	@Override
	public List<Checkin> getCheckins(List<Integer> ids) {
		return this.addDistanceToX(this.dao.getCheckins(ids));
	}
	
	@Override
	public Optional<Checkin> getTeamCheckin(int teamId, int id) {
		return this.addDistanceToX(this.dao.getTeamCheckin(teamId, id));
	}
	
	@Override
	public Optional<Checkin> getLastTeamCheckin(int teamId) {
		return this.addDistanceToX(this.dao.getLastTeamCheckin(teamId));
	}

	@Override
	public List<Checkin> getTeamCheckins(int teamId) {
		return this.addDistanceToX(this.dao.getTeamCheckins(teamId));
	}
	
	private Optional<Checkin> addDistanceToX(Optional<Checkin> maybeCheckin) {
		if (maybeCheckin.isPresent()) {
			return Optional.of(this.addDistanceToX(maybeCheckin.get()));
		}
		
		return Optional.absent();
	}
	
	private Checkin addDistanceToX(Checkin checkin) {
		double distance = distanceHelper.distanceToX(checkin);
		return checkin.toBuilder().setDistanceToX(distance).build();
	}
	
	private List<Checkin> addDistanceToX(List<Checkin> checkins) {
		List<Checkin> newCheckins = Lists.newArrayListWithCapacity(checkins.size());
		for (Checkin checkin : checkins) {
			Checkin newCheckin = this.addDistanceToX(checkin);
			newCheckins.add(newCheckin);
		}
		return newCheckins;
	}
	
}
