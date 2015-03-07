package org.jailbreak.service.db.dao.events;

import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Link;
import org.jailbreak.service.db.BindIds;
import org.jailbreak.service.db.BindProtobuf;
import org.jailbreak.service.db.mappers.events.LinkEventsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(LinkEventsMapper.class)
public interface LinkEventsDAO {	
	
	@SqlUpdate("INSERT INTO events_link (url, link_text, description, photo_url) VALUES (:url, :link_text, :description, :photo_url)")
	@GetGeneratedKeys
	public abstract int insert(@BindProtobuf Link link);
	
	@SqlQuery("SELECT * FROM events_link WHERE id = :id")
	@SingleValueResult(Link.class)
	Optional<Link> getLink(@Bind("id") int id);
	
	@SqlQuery("SELECT * FROM events_link WHERE id = ANY (:idList)")
	List<Link> getLinks(@BindIds Set<Integer> ids);

}
