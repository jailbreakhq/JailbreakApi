package org.jailbreak.service.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.TeamsFilters;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RegisterMapper(TeamsMapper.class)
public abstract class TeamsDAO {	
	
	public Connection conn;
	private final Logger LOG = LoggerFactory.getLogger(TeamsDAO.class);
	
	@SqlUpdate("INSERT INTO teams (team_name, names, team_number, avatar, tag_line, current_position, university, amount_raised_online, amount_raised_offline, countries, transport, description, featured) VALUES (:name, :names, :team_number, :avatar, :tag_line, (:current_long, :current_lat), :university, :amount_raised_online, :amount_raised_offline, :countries, :transports, :description, :featured")
	@GetGeneratedKeys
	public abstract int insert(@BindProtobuf Team team);

	@SqlUpdate("UPDATE teams SET team_name = :team_name, names = :names, team_number = :team_number, avatar = :avatar, tag_line = :tag_line, current_lat = (:current_lon, :current_lat), university = :university, amount_raised_online = :amount_raised_online, amount_raised_offline = :amount_raised_online, countries = :countries, transports = :transports, description = :description, featured = :featured WHERE id = :id")
	public abstract int update(@BindProtobuf Team team);
	
	@SqlUpdate("DELETE FROM teams WHERE id = :id")
	public abstract int delete(@Bind("id") int id);
	
	@SqlQuery("SELECT *, current_position[0] as current_x, current_position[1] as current_y FROM teams WHERE id = :id")
	@SingleValueResult(Team.class)
	public abstract Optional<Team> getTeam(@Bind("id") int id);
	
	@SqlQuery("SELECT *, current_position[0] as current_x, current_position[1] as current_y FROM teams")
	public abstract List<Team> getTeams();

	@SqlQuery("SELECT *, current_position[0] as current_x, current_position[1] as current_y FROM teams ORDER BY (amount_raised_online+amount_raised_offline) DESC LIMIT 10")
	public abstract List<Team> getTopTenTeams();
	
	public List<Team> getFilteredTeams(int limit, TeamsFilters filters) throws SQLException {
		Map<String, Object> bindParams = Maps.newHashMap();
		SimplestSqlBuilder builder = new SimplestSqlBuilder("teams")
				.addColumn("*")
				.addColumn("current_position[0] as current_x")
				.addColumn("current_position[1] as current_y");
		
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
		
		builder.addOrderBy("id", OrderBy.DESC);
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
