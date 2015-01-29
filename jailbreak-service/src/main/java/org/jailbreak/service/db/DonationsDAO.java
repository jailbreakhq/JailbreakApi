package org.jailbreak.service.db;

import java.util.List;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.service.db.mappers.DonationsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(DonationsMapper.class)
public interface DonationsDAO {	
	
	@SqlUpdate("INSERT INTO donations VALUES(null, :team_id, :amount, :name, :time, :type)")
	@GetGeneratedKeys
	int insert(@BindProtobuf Donation donation);
	
	@SqlUpdate("UPDATE donations SET team_id = :team_id, amount = :amount, name = :name, time = :time, type = :type WHERE id = :id")
	int update(@BindProtobuf Donation donation);
	
	@SqlUpdate("DELETE FROM donations WHERE id = :id")
	int delete(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM donations WHERE AND id = :id")
	@SingleValueResult(Donation.class)
	Optional<Donation> getDonation(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM donations WHERE team_id = :team_id AND id = :id")
	@SingleValueResult(Donation.class)
	Optional<Donation> getDonation(@Bind("team_id") int team_id, @Bind("id") int id);
	
	@SqlQuery("SELECT * FROM donations ORDER BY time DESC")
	List<Donation> getDonations();
	
	@SqlQuery("SELECT * FROM donations WHERE team_id = :team_id")
	List<Donation> getTeamDonations(@Bind("team_id") int team_id);
	
}
