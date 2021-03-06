package org.jailbreak.service.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Checkin;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class CheckinsMapper implements ResultSetMapper<Checkin> {
	
	@Override
	public Checkin map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		int id = r.getInt("id");
		String location = r.getString("location");
		String status = r.getString("status");
		double lat = r.getDouble("lat");
		double lon = r.getDouble("lon");
		long time = r.getLong("time");
		int team_id = r.getInt("team_id");
		
		Checkin.Builder builder = Checkin.newBuilder()
				.setId(id)
				.setLat(lat)
				.setLon(lon)
				.setTime(time)
				.setTeamId(team_id);
		
		if (status != null) {
			builder.setStatus(status);
		}
		
		if (location != null) {
			builder.setLocation(location);
		}
		
		return builder.build();
	}

}