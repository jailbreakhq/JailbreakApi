package org.jailbreak.service.db.dao.events;

import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Twitter;
import org.jailbreak.service.db.BindIds;
import org.jailbreak.service.db.mappers.events.TwitterEventsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(TwitterEventsMapper.class)
public interface TwitterEventsDAO {	
	
	@SqlQuery("SELECT * FROM events_twitter WHERE AND id = :id")
	@SingleValueResult(Twitter.class)
	Optional<Twitter> getTwitterEvent(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM events_twitter WHERE id = ANY (:idList)")
	List<Twitter> getTwitterEvents(@BindIds Set<Integer> ids);
	
}
