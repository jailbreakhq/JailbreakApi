package org.jailbreak.service.db;

import java.util.List;

import org.jailbreak.api.representations.Representations.Checkin;
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
	
	@SqlUpdate("INSERT INTO checkins (location, status, position, time, team_id) VALUES (:location, :status, (:lat, :lon), :time, :team_id)")
	@GetGeneratedKeys
	int insert(@BindProtobuf Checkin checkin);
	
	@SqlUpdate("UPDATE checkins SET location = :location, status = :status, position = :position, time = :time, team_id = :team_id WHERE id = :id")
	int update(@BindProtobuf Checkin checkin);
	
	@SqlQuery("SELECT *, position[0] as x, positon[1] as y FROM checkins WHERE AND id = :id")
	@SingleValueResult(Checkin.class)
	Optional<Checkin> getCheckin(@Bind("id") int id);
	
	@SqlQuery("SELECT *, position[0] as x, position[1] as y FROM checkins WHERE team_id = :team_id AND id = :id")
	@SingleValueResult(Checkin.class)
	Optional<Checkin> getTeamCheckin(@Bind("team_id") int team_id, @Bind("id") int id);
	
	@SqlQuery("SELECT *, position[0] as x, position[1] as y FROM checkins WHERE team_id = :team_id")
	List<Checkin> getTeamCheckins(@Bind("team_id") int team_id);
	
}
