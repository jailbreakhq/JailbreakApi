package org.jailbreak.service.db.dao.events;

import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Youtube;
import org.jailbreak.service.db.BindIds;
import org.jailbreak.service.db.mappers.events.YoutubeEventsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(YoutubeEventsMapper.class)
public interface YoutubeEventsDAO {	
	
	@SqlQuery("SELECT * FROM events_youtube WHERE id = :id")
	@SingleValueResult(Youtube.class)
	Optional<Youtube> getYoutube(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM events_youtube WHERE id = ANY (:idList)")
	List<Youtube> getYoutubes(@BindIds Set<Integer> ids);
	
}
