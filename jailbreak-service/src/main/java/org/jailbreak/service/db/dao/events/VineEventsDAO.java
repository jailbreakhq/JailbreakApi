package org.jailbreak.service.db.dao.events;

import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Vine;
import org.jailbreak.service.db.BindIds;
import org.jailbreak.service.db.mappers.events.VineEventsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(VineEventsMapper.class)
public interface VineEventsDAO {	
	
	@SqlQuery("SELECT * FROM events_vine WHERE id = :id")
	@SingleValueResult(Vine.class)
	Optional<Vine> getVine(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM events_vine WHERE id = ANY (:idList)")
	List<Vine> getVines(@BindIds Set<Integer> ids);
	
}
