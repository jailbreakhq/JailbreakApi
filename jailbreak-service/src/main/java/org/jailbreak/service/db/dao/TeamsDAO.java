package org.jailbreak.service.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.TeamsFilters;
import org.jailbreak.service.db.BindIds;
import org.jailbreak.service.db.BindProtobuf;
import org.jailbreak.service.db.ManualStatement;
import org.jailbreak.service.db.SimplestSqlBuilder;
import org.jailbreak.service.db.SimplestSqlBuilder.OrderBy;
import org.jailbreak.service.db.mappers.TeamsMapper;
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

@RegisterMapper(TeamsMapper.class)
public abstract class TeamsDAO {	
	
	public Connection conn;
	private final Logger LOG = LoggerFactory.getLogger(TeamsDAO.class);
	
	@SqlUpdate("INSERT INTO teams (team_name, names, team_number, avatar, tag_line, university, amount_raised_online, amount_raised_offline, countries, transport, description, featured, slug, video, avatar_large, last_checkin_id) VALUES (:name, :names, :team_number, :avatar, :tag_line, :university, :amount_raised_online, :amount_raised_offline, :countries, :transports, :description, :featured, :slug, :video, :avatar_large, :last_checkin_id")
	@GetGeneratedKeys
	public abstract int insert(@BindProtobuf Team team);

	@SqlUpdate("UPDATE teams SET team_name = :team_name, names = :names, team_number = :team_number, avatar = :avatar, tag_line = :tag_line, university = :university, amount_raised_online = :amount_raised_online, amount_raised_offline = :amount_raised_offline, countries = :countries, transports = :transports, description = :description, featured = :featured, slug = :slug, video = :video, avatar_large = :avatar_large, last_checkin_id = :last_checkin_id WHERE id = :id")
	public abstract int update(@BindProtobuf Team team);
	
	@SqlUpdate("DELETE FROM teams WHERE id = :id")
	public abstract int delete(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM teams WHERE id = :id")
	@SingleValueResult(Team.class)
	public abstract Optional<Team> getTeam(@Bind("id") int id);
	
	@SqlQuery("SELECT id, team_number, team_name, names, slug, avatar, tag_line, university, amount_raised_online, amount_raised_offline, countries, transports, featured, last_checkin_id FROM teams WHERE id = :id")
	@SingleValueResult(Team.class)
	public abstract Optional<Team> getLimitedTeam(@Bind("id") int id);
	
	@SqlQuery("SELECT id, team_number, team_name, names, slug, avatar, tag_line, university, amount_raised_online, amount_raised_offline, countries, transports, featured, last_checkin_id FROM teams WHERE id = ANY (:idList)")
	@SingleValueResult(Team.class)
	public abstract List<Team> getLimitedTeams(@BindIds Set<Integer> ids);
	
	@SqlQuery("SELECT * FROM teams WHERE slug = :slug")
	@SingleValueResult(Team.class)
	public abstract Optional<Team> getTeamSlug(@Bind("slug") String slug);
	
	@SqlQuery("SELECT * FROM teams ORDER BY (amount_raised_online + amount_raised_offline) DESC")
	public abstract List<Team> getTeams();

	@SqlQuery("SELECT * FROM teams ORDER BY (amount_raised_online + amount_raised_offline) DESC LIMIT 10")
	public abstract List<Team> getTopTenTeams();
	
	public List<Team> getFilteredTeams(int limit, TeamsFilters filters) throws SQLException {
		Map<String, Object> bindParams = Maps.newHashMap();
		SimplestSqlBuilder builder = new SimplestSqlBuilder("teams");
		
		if (filters.hasTeamNumber()) {
			builder.addWhere("team_number = :team_number");
			bindParams.put("team_number", filters.getTeamNumber());
		}
		
		if (filters.hasUniversity()) {
			builder.addWhere("university = :university");
			bindParams.put("university", filters.getUniversity().ordinal());
		}
		
		if (filters.hasFeatured()) {
			builder.addWhere("featured = :featured");
			bindParams.put("featured", filters.getFeatured());
		}
		
		builder.addOrderBy("(amount_raised_online + amount_raised_offline)", OrderBy.DESC);
		builder.limit(limit);
		
		String queryString = builder.build();
		
		LOG.debug("getFilteredTeams SQL: " + queryString);
		
		try {
			ManualStatement query = new ManualStatement(conn, queryString, bindParams);
			List<Team> results = query.executeQuery(new TeamsMapper());
			return results;
		} catch (SQLException e) {
			LOG.error("SQL Error executing query getFilteredDonations " + e.getMessage());
			throw e;
		}
	}
	
}
