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
		
		Donation.Builder builder = Donation.newBuilder()
				.setId(id)
				.setTeamId(team_id)
				.setAmount(amount)
				.setTime(time)
				.setType(type);
		
		if (!name.isEmpty()) {
			builder.setName(name);
		}
		
		return builder.build();
	}

}
