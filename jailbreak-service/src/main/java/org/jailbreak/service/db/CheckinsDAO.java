package org.jailbreak.service.db;

import java.util.List;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.service.db.mappers.CheckinsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(CheckinsMapper.class)
public interface CheckinsDAO {	
	
	@SqlUpdate("INSERT INTO checkins (location, status, position, time, team_id) VALUES (:location, :status, :position, :time, :team_id)")
	int insert(@Bind("location") String location, @Bind("status") String status, @Bind("time") String time, @Bind("team_id") String team_id);
	
	@SqlQuery("SELECT *, X(position) as x, Y(position) as y FROM checkins WHERE team_id = :team_id AND id = :id")
	@SingleValueResult(Checkin.class)
	Optional<Checkin> getCheckin(@Bind("team_id") int team_id, @Bind("id") int id);
	
	@SqlQuery("SELECT *, X(position) as x, Y(position) as y FROM checkins WHERE team_id = :team_id")
	List<Checkin> getTeamCheckins(@Bind("team_id") int team_id);
	
}
