package org.jailbreak.service.resources.events;

import io.dropwizard.auth.Auth;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jailbreak.api.representations.Representations.Link;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.api.representations.Representations.User.UserLevel;
import org.jailbreak.service.core.events.LinkEventsManager;
import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.errors.BadRequestException;
import org.jailbreak.service.errors.ForbiddenException;
import org.jailbreak.service.resources.Paths;

import com.google.inject.Inject;

@Path(Paths.LINK_EVENTS_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class LinkEventsResource {
	
	private final LinkEventsManager manager;
	
	@Inject
	public LinkEventsResource(LinkEventsManager manager) {
		this.manager = manager;
	}
	
	@POST
	public Link createLink(@Auth User user, Link link) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to create a Link Event", ApiDocs.EVENTS);
		}
		
		if (!link.hasUrl()) {
			throw new BadRequestException("You must provide a URL to create this Link Event", ApiDocs.EVENTS);
		}
		
		if (!link.hasDescription()) {
			throw new BadRequestException("You must provide a description to create this Link Event", ApiDocs.EVENTS);
		}
		
		return manager.createLinkEvent(link);
	}

}
