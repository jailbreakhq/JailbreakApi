package org.jailbreak.service.db.dao;

import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.service.db.BindIds;
import org.jailbreak.service.db.BindProtobuf;
import org.jailbreak.service.db.mappers.CheckinsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(CheckinsMapper.class)
public interface CheckinsDAO {	
	
	@SqlUpdate("INSERT INTO checkins (location, status, lat, lon, time, team_id) VALUES (:location, :status, :lat, :lon, extract(epoch from now() at time zone 'utc'), :team_id)")
	@GetGeneratedKeys
	int insert(@BindProtobuf Checkin checkin);
	
	@SqlUpdate("UPDATE checkins SET location = :location, status = :status, lat = :lat, lon = :lon, time = :time, team_id = :team_id WHERE id = :id")
	int update(@BindProtobuf Checkin checkin);
	
	@SqlQuery("SELECT * FROM checkins WHERE id = :id")
	@SingleValueResult(Checkin.class)
	Optional<Checkin> getCheckin(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM checkins WHERE id = ANY (:idList)")
	List<Checkin> getCheckins(@BindIds Set<Integer> ids);
	
	@SqlQuery("SELECT * FROM checkins WHERE team_id = :team_id AND id = :id ORDER BY time DESC")
	@SingleValueResult(Checkin.class)
	Optional<Checkin> getTeamCheckin(@Bind("team_id") int teamId, @Bind("id") int id);
	
	@SqlQuery("SELECT * FROM checkins WHERE team_id = :team_id ORDER BY id DESC LIMIT 1")
	@SingleValueResult(Checkin.class)
	Optional<Checkin> getLastTeamCheckin(@Bind("team_id") int teamId);
	
	@SqlQuery("SELECT * FROM checkins WHERE team_id = :team_id")
	List<Checkin> getTeamCheckins(@Bind("team_id") int teamId);
	
}
