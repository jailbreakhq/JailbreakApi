package org.jailbreak.service.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Link;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class LinkEventsMapper implements ResultSetMapper<Link> {
	
	public Link map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		int id = r.getInt("id");
		String url = r.getString("url");
		String linkText = r.getString("link_text");
		String description = r.getString("description");
		String photoUrl = r.getString("photo_url");
		
		Link.Builder builder = Link.newBuilder()
				.setId(id)
				.setUrl(url)
				.setLinkText(linkText);
		
		if (description != null && !description.isEmpty()) {
			builder.setDescription(description);
		}
		
		if (photoUrl != null && !photoUrl.isEmpty()) {
			builder.setPhotoUrl(photoUrl);
		}
		
		return builder.build();
	}

}
