package org.jailbreak.service.db.dao.events;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Event.EventsFilters;
import org.jailbreak.service.db.BindProtobuf;
import org.jailbreak.service.db.ManualStatement;
import org.jailbreak.service.db.SimplestSqlBuilder;
import org.jailbreak.service.db.SimplestSqlBuilder.OrderBy;
import org.jailbreak.service.db.mappers.RowCountMapper;
import org.jailbreak.service.db.mappers.events.EventsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

@RegisterMapper(EventsMapper.class)
public abstract class EventsDAO {
	
	public Connection conn;
	private final Logger LOG = LoggerFactory.getLogger(EventsDAO.class);
	
	@SqlUpdate("INSERT INTO events (type, object_id, time, team_id, highlight) VALUES (:type, :object_id, extract(epoch from now() at time zone 'utc'), :team_id, :highlight)")
	@GetGeneratedKeys
	public abstract int insert(@BindProtobuf Event event);

	@SqlUpdate("UPDATE events SET type = :type, object_id = :object_id, time = :time, team_id = :team_id, highlight = :highlight WHERE id = :id")
	public abstract int update(@BindProtobuf Event event);
	
	@SqlQuery("SELECT * FROM events WHERE id = :id")
	@SingleValueResult(Event.class)
	public abstract Optional<Event> getEvent(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM events ORDER BY time DESC LIMIT :limit")
	public abstract List<Event> getEvents(@Bind("limit") int limit);
	
	public List<Event> getFilteredEvents(int limit, EventsFilters filters) throws SQLException {
		// Build query and bind in params
		Map<String, Object> bindParams = Maps.newHashMap();
		SimplestSqlBuilder builder = applyWhereFilters(filters, bindParams);
		builder.addOrderBy("time", OrderBy.DESC);
		builder.limit(limit);
		String queryString = builder.build();
		
		LOG.debug("getFilteredEvents SQL: " + queryString);
		
		ManualStatement query = new ManualStatement(conn, queryString, bindParams);
		List<Event> results = query.executeQuery(new EventsMapper());
		return results;
	}
	
	public int countFilteredEvents(EventsFilters filters) throws SQLException {
		// Build query and bind in params
		Map<String, Object> bindParams = Maps.newHashMap();
		SimplestSqlBuilder builder = applyWhereFilters(filters, bindParams);
		builder.addColumn("COUNT(*) as count");
		String queryString = builder.build();
		
		LOG.debug("countFilteredEvents SQL: " + queryString);
		
		ManualStatement query = new ManualStatement(conn, queryString, bindParams);
		Integer count = query.executeQuery(new RowCountMapper()).get(0);
		return count;
	}
	
	private SimplestSqlBuilder applyWhereFilters(EventsFilters filters, Map<String, Object> bindParams) {
		SimplestSqlBuilder builder = new SimplestSqlBuilder("events");
		
		if (filters.hasTeamId()) {
			builder.addWhere("team_id = :team_id");
			bindParams.put("team_id", filters.getTeamId());
		}
		if (filters.hasType()) {
			builder.addWhere("type = :type");
			bindParams.put("type", filters.getType().ordinal());
		}
		if (filters.hasBeforeId()) {
			builder.addWhere("id < :before_id");
			bindParams.put("before_id", filters.getBeforeId());
		}
		if (filters.hasAfterId()) {
			builder.addWhere("id > :after_id");
			bindParams.put("after_id", filters.getAfterId());
		}
		if (filters.hasHighlight()) {
			builder.addWhere("highlight = :highlight");
			bindParams.put("highlight", filters.getHighlight());
		}
		
		return builder;
	}
	
}
