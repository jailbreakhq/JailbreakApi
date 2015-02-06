package org.jailbreak.service.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.University;
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
		String current_x = r.getString("current_x");
		String current_y = r.getString("current_y");
		University university = University.valueOf(r.getInt("university"));
		int amount_raised_online = r.getInt("amount_raised_online");
		int amount_raised_offline = r.getInt("amount_raised_offline");
		int countries = r.getInt("countries");
		int transports = r.getInt("transports");
		String description = r.getString("description");
		boolean featured = r.getBoolean("featured");
		String slug = r.getString("slug");
		String video = r.getString("video");
		String avatar_large = r.getString("avatar_large");
		
		Team.Builder builder = Team.newBuilder()
				.setId(id)
				.setTeamNumber(team_number)
				.setTeamName(team_name)
				.setNames(names)
				.setCurrentLat(Double.parseDouble(current_x))
				.setCurrentLon(Double.parseDouble(current_y))
				.setUniversity(university)
				.setAmountRaisedOnline(amount_raised_online)
				.setAmountRaisedOffline(amount_raised_offline)
				.setCountries(countries)
				.setTransports(transports)
				.setFeatured(featured)
				.setSlug(slug);
		
		if (tag_line != null) {
			builder.setTagLine(tag_line);
		}
		
		if (avatar != null) {
			builder.setAvatar(avatar);
		}
		
		if (description != null) {
			builder.setDescription(description);
		}
		
		if (video != null) {
			builder.setVideo(video);
		}
		
		if (avatar_large != null) {
			builder.setAvatarLarge(avatar_large);
		}
		
		return builder.build();
	}

}
