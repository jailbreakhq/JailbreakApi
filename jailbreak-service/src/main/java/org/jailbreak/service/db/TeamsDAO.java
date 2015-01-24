package org.jailbreak.service.db;

import java.util.List;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.service.db.mappers.TeamsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(TeamsMapper.class)
public interface TeamsDAO {	
	
	@SqlUpdate("INSERT INTO teams (team_name, names, team_number, avatar, tag_line, start_position, current_position) VALUES (:name, :names, :team_number, :avatar, :tag_line, ST_SetSRID(ST_MakePoint(:start_lon, :start_lat)), ST_SetSRID(ST_MakePoint(:current_long, :current_lat))")
	@GetGeneratedKeys
	int addTeam(@BindProtobuf Team team);

	@SqlUpdate("UPDATE teams SET team_name = :team_name, names = :names, team_number = :team_number, avatar = :avatar, tag_line = :tag_line, start_position = ST_SetSRID(ST_MakePoint(:start_lon, :start_lat)), current_lat = ST_SetSRID(ST_MakePoint(:current_lon, :current_lat)), university = :university WHERE id = :cid")
	int updateTeam(@Bind("cid") int id, @BindProtobuf Team team);
	
	@SqlQuery("SELECT *, start_position[0] as start_x, start_position[1] start_y, current_position[0] as current_x, current_position[1] as current_y FROM teams WHERE id = :id")
	@SingleValueResult(Team.class)
	Optional<Team> getTeam(@Bind("id") int id);
	
	@SqlQuery("SELECT *, start_position[0] as start_x, start_position[1] start_y, current_position[0] as current_x, current_position[1] as current_y FROM teams")
	List<Team> getTeams();
	
}
