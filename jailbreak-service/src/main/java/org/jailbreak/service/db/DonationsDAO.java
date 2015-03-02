package org.jailbreak.service.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Donation.DonationsFilters;
import org.jailbreak.service.db.SimplestSqlBuilder.OrderBy;
import org.jailbreak.service.db.mappers.DonationsMapper;
import org.jailbreak.service.db.mappers.RowCountMapper;
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

@RegisterMapper(DonationsMapper.class)
public abstract class DonationsDAO {
	
	public Connection conn;
	private final Logger LOG = LoggerFactory.getLogger(DonationsDAO.class);
	
	@SqlUpdate("INSERT INTO donations (team_id, amount, name, time, type, email) VALUES(:team_id, :amount, :name, :time, :type, :email)")
	@GetGeneratedKeys
	public abstract int insert(@BindProtobuf Donation donation);
	
	@SqlUpdate("UPDATE donations SET team_id = :team_id, amount = :amount, name = :name, time = :time, type = :type, email = :email WHERE id = :id")
	public abstract int update(@BindProtobuf Donation donation);
	
	@SqlUpdate("DELETE FROM donations WHERE id = :id")
	public abstract int delete(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM donations WHERE id = :id")
	@SingleValueResult(Donation.class)
	public abstract Optional<Donation> getDonation(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM donations ORDER BY time DESC LIMIT :limit")
	public abstract List<Donation> getDonations(@Bind("limit") int limit);
	
	@SqlQuery("SELECT SUM(amount) FROM donations")
	public abstract int getDonationsTotalAmount();
	
	public List<Donation> getFilteredDonations(int limit, DonationsFilters filters) throws SQLException {
		// Build query and bind in params
		Map<String, Object> bindParams = Maps.newHashMap();
		SimplestSqlBuilder builder = applyWhereFilters(filters, bindParams);
		builder.addOrderBy("time", OrderBy.DESC);
		builder.limit(limit);
		String queryString = builder.build();
		
		LOG.debug("getFilteredDonations SQL: " + queryString);
		
		ManualStatement query = new ManualStatement(conn, queryString, bindParams);
		List<Donation> results = query.executeQuery(new DonationsMapper());
		return results;
	}
	
	public int countFilteredDonations(DonationsFilters filters) throws SQLException {
		// Build query and bind in params
		Map<String, Object> bindParams = Maps.newHashMap();
		SimplestSqlBuilder builder = applyWhereFilters(filters, bindParams);
		builder.addColumn("COUNT(*) as count");
		String queryString = builder.build();
		
		LOG.debug("countFilteredDonations SQL: " + queryString);
		
		ManualStatement query = new ManualStatement(conn, queryString, bindParams);
		Integer count = query.executeQuery(new RowCountMapper()).get(0);
		return count;
	}
	
	private SimplestSqlBuilder applyWhereFilters(DonationsFilters filters, Map<String, Object> bindParams) {
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
		
		return builder;
	}
	
}
