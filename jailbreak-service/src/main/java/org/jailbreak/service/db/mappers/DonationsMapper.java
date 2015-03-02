package org.jailbreak.service.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Donation.DonationType;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class DonationsMapper implements ResultSetMapper<Donation> {
	
	public Donation map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		int id = r.getInt("id");
		int team_id = r.getInt("team_id");
		int amount = r.getInt("amount");
		String name = r.getString("name");
		long time = r.getLong("time");
		DonationType type = DonationType.valueOf(r.getInt("type"));
		String email = r.getString("email");
		
		Donation.Builder builder = Donation.newBuilder()
				.setId(id)
				.setAmount(amount)
				.setTime(time)
				.setType(type);
		
		if (team_id != 0) {
			builder.setTeamId(team_id);
		}
		
		if (name != null) {
			builder.setName(name);
		}
		
		if (email != null) {
			builder.setEmail(email);
		}
		
		return builder.build();
	}

}
