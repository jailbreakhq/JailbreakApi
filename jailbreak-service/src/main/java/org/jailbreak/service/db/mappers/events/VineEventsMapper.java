package org.jailbreak.service.db.mappers.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Vine;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class VineEventsMapper implements ResultSetMapper<Vine> {
	
	public Vine map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		int id = r.getInt("id");
		String description = r.getString("description");
		String url = r.getString("url");
		String thumbnailUrl = r.getString("thumbnail_url");
		String iframeHtml = r.getString("iframe_html");
		String authorName = r.getString("author_name");
		String authorUrl = r.getString("author_url");
		String authorPhotoUrl = r.getString("author_photo_url");
		long time = r.getLong("time");
		int teamId = r.getInt("team_id");
		
		Vine.Builder builder = Vine.newBuilder()
				.setId(id)
				.setUrl(url)
				.setThumbnailUrl(thumbnailUrl)
				.setTime(time);
		
		if (description != null && !description.isEmpty()) {
			builder.setDescription(description);
		}
		
		if (iframeHtml != null && !iframeHtml.isEmpty()) {
			builder.setIframeHtml(iframeHtml);
		}
		
		if (authorName != null && !authorName.isEmpty()) {
			builder.setAuthorName(authorName);
		}
		
		if (authorUrl != null && !authorUrl.isEmpty()) {
			builder.setAuthorUrl(authorUrl);
		}
		
		if (authorPhotoUrl != null && !authorPhotoUrl.isEmpty()) {
			builder.setAuthorPhotoUrl(authorPhotoUrl);
		}
		
		if (teamId != 0) {
			builder.setTeamId(teamId);
		}
		
		return builder.build();
	}

}
