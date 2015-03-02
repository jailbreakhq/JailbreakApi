package org.jailbreak.service.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.University;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class TeamsMapper implements ResultSetMapper<Team> {
	
	public Team map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		// Limited Team Object Attributes
		int id = r.getInt("id");
		int team_number = r.getInt("team_number");
		String team_name = r.getString("team_name");
		String names = r.getString("names");
		String avatar = r.getString("avatar");
		String tagLine = r.getString("tag_line");
		University university = University.valueOf(r.getInt("university"));
		int amountRaisedOnline = r.getInt("amount_raised_online");
		int amountRaisedOffline = r.getInt("amount_raised_offline");
		int countries = r.getInt("countries");
		int transports = r.getInt("transports");
		boolean featured = r.getBoolean("featured");
		String slug = r.getString("slug");
		int lastCheckinId = r.getInt("last_checkin_id");
		
		Team.Builder builder = Team.newBuilder()
				.setId(id)
				.setTeamNumber(team_number)
				.setTeamName(team_name)
				.setNames(names)
				.setUniversity(university)
				.setAmountRaisedOnline(amountRaisedOnline)
				.setAmountRaisedOffline(amountRaisedOffline)
				.setCountries(countries)
				.setTransports(transports)
				.setFeatured(featured)
				.setSlug(slug);
		
		if (lastCheckinId != 0) {
			builder.setLastCheckinId(lastCheckinId);
		}
		
		if (tagLine != null && !tagLine.isEmpty()) {
			builder.setTagLine(tagLine);
		}
		
		if (avatar != null && !avatar.isEmpty()) {
			builder.setAvatar(avatar);
		}
		
		
		// Full Team Object Attributes
		boolean fullTeam = true;
		try {
			// simple test that will fail on the limited team object result set
			r.getString("description");
		} catch (SQLException e) {
			// Burying SQLException because we maybe trying to map a limited team instead of a full team
			fullTeam = false;
		}
		
		if (fullTeam) {
			String description = r.getString("description");
			String video = r.getString("video");
			String avatarLarge = r.getString("avatar_large");
			
			if (description != null && !description.isEmpty()) {
				builder.setDescription(description);
			}
			
			if (video != null && !video.isEmpty()) {
				builder.setVideo(video);
			}
			
			if (avatarLarge != null && !avatarLarge.isEmpty()) {
				builder.setAvatarLarge(avatarLarge);
			}
		}
		
		
		return builder.build();
	}

}
