package org.jailbreak.service.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Donation.DonationsFilters;
import org.jailbreak.service.db.mappers.DonationsMapper;
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

@RegisterMapper(DonationsMapper.class)
public abstract class DonationsDAO {
	
	public Connection conn;
	private final Logger LOG = LoggerFactory.getLogger(DonationsDAO.class);
	
	@SqlUpdate("INSERT INTO donations VALUES(null, :team_id, :amount, :name, :time, :type)")
	@GetGeneratedKeys
	public abstract int insert(@BindProtobuf Donation donation);
	
	@SqlUpdate("UPDATE donations SET team_id = :team_id, amount = :amount, name = :name, time = :time, type = :type WHERE id = :id")
	public abstract int update(@BindProtobuf Donation donation);
	
	@SqlUpdate("DELETE FROM donations WHERE id = :id")
	public abstract int delete(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM donations WHERE AND id = :id")
	@SingleValueResult(Donation.class)
	public abstract Optional<Donation> getDonation(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM donations ORDER BY time DESC")
	public abstract List<Donation> getDonations();
	
	public List<Donation> getFilteredDonations(DonationsFilters filters) {
		// Build query and bind in params
		Map<String, Object> bindParams = Maps.newHashMap();
		SimplestSqlBuilder builder = new SimplestSqlBuilder("donations");
		
		if (filters.hasTeamId()) {
			builder.addWhere("team_id = :team_id");
			bindParams.put("team_id", filters.getTeamId());
		}
		if (filters.hasSinceTime()) {
			builder.addWhere("time > :time_since");
			bindParams.put("time_since", filters.getSinceTime());
		}
		if (filters.hasType()) {
			builder.addWhere("type = :type");
			bindParams.put("type", filters.getType().ordinal());
		}
		if (filters.hasLimit()) {
			builder.limit(filters.getLimit());
		}
		String queryString = builder.build();
		
		LOG.debug("getFilteredDonations: " + queryString);
		
		try {
			ManualStatement query = new ManualStatement(conn, queryString, bindParams);
			List<Donation> results = query.executeQuery(new DonationsMapper());
			return results;
		} catch (SQLException e) {
			LOG.error("SQL Error executing query getFilteredDonations");
		}
		
		return Lists.newArrayListWithCapacity(0);
	}
	
}
