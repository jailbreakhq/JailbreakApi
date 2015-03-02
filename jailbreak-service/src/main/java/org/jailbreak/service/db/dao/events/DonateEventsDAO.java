package org.jailbreak.service.db.dao.events;

import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Donate;
import org.jailbreak.service.db.BindIds;
import org.jailbreak.service.db.mappers.events.DonateEventsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(DonateEventsMapper.class)
public interface DonateEventsDAO {	
	
	@SqlQuery("SELECT * FROM events_donate WHERE AND id = :id")
	@SingleValueResult(Donate.class)
	Optional<Donate> getDonateEvent(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM events_donate WHERE id = ANY (:idList)")
	List<Donate> getDonateEvents(@BindIds Set<Integer> ids);
	
}
