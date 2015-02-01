package org.jailbreak.service.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Donation.DonationsFilters;
import org.jailbreak.service.db.mappers.DonationsMapper;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
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
import com.newrelic.deps.com.google.common.collect.Lists;

@RegisterMapper(DonationsMapper.class)
public abstract class DonationsDAO {
	
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
	
	public List<Donation> getFilteredDonations(Connection conn, DonationsFilters filters) throws SQLException {
		// Build query and bind in params
		Map<String, Object> bindValues = Maps.newHashMap();
		SimplestSqlBuilder builder = new SimplestSqlBuilder("donations");
		
		if (filters.hasTeamId()) {
			builder.addWhere("team_id = :team_id");
			bindValues.put("team_id", filters.getTeamId());
		}
		if (filters.hasSinceTime()) {
			builder.addWhere("time > :time_since");
			bindValues.put("time_since", filters.getSinceTime());
		}
		if (filters.hasType()) {
			builder.addWhere("type = :type");
			bindValues.put("type", filters.getType().ordinal());
		}
		if (filters.hasLimit()) {
			builder.limit(filters.getLimit());
		}
		String queryString = builder.build();
		
		LOG.info("getFilteredDonations: " + queryString);

		// Bind params and execute the query
		NamedParamStatement stmt = new NamedParamStatement(conn, queryString);
		for (Map.Entry<String, Object> entry : bindValues.entrySet()) {
			stmt.bind(entry.getKey(), entry.getValue());
		}
		
		ResultSet rs = stmt.executeQuery();
		List<Donation> results = Lists.newArrayList();
		DonationsMapper mapper = new DonationsMapper();
		int i = 0;
		while(rs.next()) {
			Donation donation = mapper.map(i, rs, null);
			results.add(donation);
		}
		
		return results;
	}
	
}
