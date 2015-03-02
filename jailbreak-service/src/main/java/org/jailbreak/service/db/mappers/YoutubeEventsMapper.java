package org.jailbreak.service.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Youtube;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class YoutubeEventsMapper implements ResultSetMapper<Youtube> {
	
	public Youtube map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		int id = r.getInt("id");
		String title = r.getString("title");
		String description = r.getString("description");
		String url = r.getString("url");
		String thumbnailUrl = r.getString("thumbnail_url");
		String iframeHtml = r.getString("iframe_html");
		String authorName = r.getString("author_name");
		String authorUrl = r.getString("author_url");
		int team_id = r.getInt("team_id");
		
		Youtube.Builder builder = Youtube.newBuilder()
				.setId(id)
				.setTitle(title)
				.setUrl(url)
				.setThumbnailUrl(thumbnailUrl);
		
		if (team_id != 0) {
			builder.setTeamId(team_id);
		}
		
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
		
		return builder.build();
	}

}
