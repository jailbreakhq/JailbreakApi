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
	
	@SqlUpdate("INSERT INTO teams (team_name, names, team_number, avatar, tag_line, start_position, current_position, university, amount_raised_online, amount_raised_offline, countries, transport, description) VALUES (:name, :names, :team_number, :avatar, :tag_line, (:start_lon, :start_lat), (:current_long, :current_lat), :university, :amount_raised_online, :amount_raised_offline, :countries, :transports, :description")
	@GetGeneratedKeys
	int insert(@BindProtobuf Team team);

	@SqlUpdate("UPDATE teams SET team_name = :team_name, names = :names, team_number = :team_number, avatar = :avatar, tag_line = :tag_line, start_position = (:start_lon, :start_lat), current_lat = (:current_lon, :current_lat), university = :university, amount_raised_online = :amount_raised_online, amount_raised_offline = :amount_raised_online, countries = :countries, transports = :transports, description = :description WHERE id = :id")
	int update(@BindProtobuf Team team);
	
	@SqlUpdate("DELETE FROM teams WHERE id = :id")
	int delete(@Bind("id") int id);
	
	@SqlQuery("SELECT *, start_position[0] as start_x, start_position[1] start_y, current_position[0] as current_x, current_position[1] as current_y FROM teams WHERE id = :id")
	@SingleValueResult(Team.class)
	Optional<Team> getTeam(@Bind("id") int id);
	
	@SqlQuery("SELECT *, start_position[0] as start_x, start_position[1] start_y, current_position[0] as current_x, current_position[1] as current_y FROM teams")
	List<Team> getTeams();

	@SqlQuery("SELECT *, start_position[0] as start_x, start_position[1] start_y, current_position[0] as current_x, current_position[1] as current_y FROM teams ORDER BY (amount_raised_online+amount_raised_offline) DESC LIMIT 10")
	List<Team> getTopTenTeams();
	
}
