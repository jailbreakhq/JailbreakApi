package org.jailbreak.service.db.dao.events;

import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Instagram;
import org.jailbreak.service.db.BindIds;
import org.jailbreak.service.db.mappers.events.InstagramEventsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(InstagramEventsMapper.class)
public interface InstagramEventsDAO {	
	
	@SqlQuery("SELECT * FROM events_instagram WHERE id = :id")
	@SingleValueResult(Instagram.class)
	Optional<Instagram> getInstagram(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM events_instagram WHERE id = ANY (:idList)")
	List<Instagram> getInstagrams(@BindIds Set<Integer> ids);
	
}
