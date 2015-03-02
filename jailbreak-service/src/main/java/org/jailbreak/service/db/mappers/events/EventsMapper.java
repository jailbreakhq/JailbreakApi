package org.jailbreak.service.db.mappers.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Event.EventType;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class EventsMapper implements ResultSetMapper<Event> {
	
	public Event map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		int id = r.getInt("id");
		long time = r.getLong("time");
		int objectId = r.getInt("object_id");
		EventType type = EventType.valueOf(r.getInt("type"));
		int teamId = r.getInt("team_id");
		
		Event.Builder builder = Event.newBuilder()
				.setId(id)
				.setTime(time)
				.setType(type)
				.setObjectId(objectId);
		
		if (teamId != 0) {
			builder.setTeamId(teamId);
		}
		
		return builder.build();
	}

}
