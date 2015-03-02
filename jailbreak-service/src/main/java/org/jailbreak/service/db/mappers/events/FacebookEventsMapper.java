package org.jailbreak.service.db.mappers.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Facebook;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class FacebookEventsMapper implements ResultSetMapper<Facebook> {
	
	public Facebook map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		int id = r.getInt("id");
		String facebookId = r.getString("facebook_id");
		String url = r.getString("url");
		String message = r.getString("message");
		String linkUrl = r.getString("link_url");
		String photoUrl = r.getString("photo_url");
		String pageName = r.getString("page_name");
		long time = r.getLong("time");
		int teamId = r.getInt("team_id");
		
		Facebook.Builder builder = Facebook.newBuilder()
				.setId(id)
				.setFacebookId(facebookId)
				.setUrl(url)
				.setTime(time);
		
		if (message != null && !message.isEmpty()) {
			builder.setMessage(message);
		}
		
		if (linkUrl != null && !linkUrl.isEmpty()) {
			builder.setLinkUrl(linkUrl);
		}
		
		if (photoUrl != null && !photoUrl.isEmpty()) {
			builder.setPhotoUrl(photoUrl);
		}
		
		if (pageName != null && !pageName.isEmpty()) {
			builder.setPageName(pageName);
		}
		
		if (teamId != 0) {
			builder.setTeamId(teamId);
		}
		
		return builder.build();
	}

}
