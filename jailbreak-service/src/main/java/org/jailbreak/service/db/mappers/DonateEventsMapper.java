package org.jailbreak.service.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Donate;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class DonateEventsMapper implements ResultSetMapper<Donate> {
	
	public Donate map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		int id = r.getInt("id");
		String linkText = r.getString("link_text");
		String description = r.getString("description");
		int teamId = r.getInt("team_id");
		
		Donate.Builder builder = Donate.newBuilder()
				.setId(id)
				.setLinkText(linkText);
		
		if (description != null && !description.isEmpty()) {
			builder.setDescription(description);
		}
		
		if (teamId != 0) {
			builder.setTeamId(teamId);
		}
		
		return builder.build();
	}

}
