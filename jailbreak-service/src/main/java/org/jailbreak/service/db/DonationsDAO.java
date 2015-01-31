package org.jailbreak.service.db;

import java.util.List;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Donation.DonationsFilters;
import org.jailbreak.service.db.mappers.DonationsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;
import com.newrelic.deps.com.google.common.collect.Lists;

@RegisterMapper(DonationsMapper.class)
public abstract class DonationsDAO {	
	
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
		List<Donation> results = Lists.newArrayList();
		
		
		
		return results;
	}
	
}
