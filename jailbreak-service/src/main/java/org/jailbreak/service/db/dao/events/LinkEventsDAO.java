package org.jailbreak.service.db.dao.events;

import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Link;
import org.jailbreak.service.db.BindIds;
import org.jailbreak.service.db.mappers.events.LinkEventsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(LinkEventsMapper.class)
public interface LinkEventsDAO {	
	
	@SqlQuery("SELECT * FROM events_link WHERE AND id = :id")
	@SingleValueResult(Link.class)
	Optional<Link> getLink(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM events_link WHERE id = ANY (:idList)")
	List<Link> getLinks(@BindIds Set<Integer> ids);
	
}
