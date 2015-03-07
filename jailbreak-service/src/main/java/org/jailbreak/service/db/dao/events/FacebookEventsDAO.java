package org.jailbreak.service.db.dao.events;

import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Facebook;
import org.jailbreak.service.db.BindIds;
import org.jailbreak.service.db.mappers.events.FacebookEventsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(FacebookEventsMapper.class)
public interface FacebookEventsDAO {	
	
	@SqlQuery("SELECT * FROM events_facebook WHERE id = :id")
	@SingleValueResult(Facebook.class)
	Optional<Facebook> getFacebookEvent(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM events_facebook WHERE id = ANY (:idList)")
	List<Facebook> getFacebookEvents(@BindIds Set<Integer> ids);
	
}
