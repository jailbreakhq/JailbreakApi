package org.jailbreak.service.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Team;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class TeamsMapper implements ResultSetMapper<Team> {
	
	public Team map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		int id = r.getInt("id");
		int team_number = r.getInt("team_number");
		String team_name = r.getString("team_name");
		String names = r.getString("names");
		String avatar = r.getString("avatar");
		String tag_line = r.getString("tag_line");
		String start_x = r.getString("start_x");
		String start_y = r.getString("start_y");
		String current_x = r.getString("current_x");
		String current_y = r.getString("current_y");
		
		return Team.newBuilder()
				.setId(id)
				.setTeamNumber(team_number)
				.setTeamName(team_name)
				.setNames(names)
				.setAvatar(avatar)
				.setTagLine(tag_line)
				.setStartLat(Double.parseDouble(start_x))
				.setStartLon(Double.parseDouble(start_y))
				.setCurrentLat(Double.parseDouble(current_x))
				.setCurrentLon(Double.parseDouble(current_y))
				.build();
	}

}
