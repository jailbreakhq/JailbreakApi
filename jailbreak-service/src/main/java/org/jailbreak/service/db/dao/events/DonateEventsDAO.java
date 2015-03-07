package org.jailbreak.service.db.dao.events;

import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Donate;
import org.jailbreak.service.db.BindIds;
import org.jailbreak.service.db.BindProtobuf;
import org.jailbreak.service.db.mappers.events.DonateEventsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(DonateEventsMapper.class)
public interface DonateEventsDAO {
	
	@SqlUpdate("INSERT INTO events_donate (link_text, description, team_id) VALUES (:link_text, :description, :team_id)")
	@GetGeneratedKeys
	public abstract int insert(@BindProtobuf Donate donate);
	
	@SqlQuery("SELECT * FROM events_donate WHERE id = :id")
	@SingleValueResult(Donate.class)
	Optional<Donate> getDonateEvent(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM events_donate WHERE id = ANY (:idList)")
	List<Donate> getDonateEvents(@BindIds Set<Integer> ids);

}
