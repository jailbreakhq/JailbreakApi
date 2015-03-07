package org.jailbreak.service.resources.events;

import io.dropwizard.auth.Auth;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jailbreak.api.representations.Representations.Donate;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.api.representations.Representations.User.UserLevel;
import org.jailbreak.service.core.events.DonateEventsManager;
import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.errors.BadRequestException;
import org.jailbreak.service.errors.ForbiddenException;
import org.jailbreak.service.resources.Paths;

import com.google.inject.Inject;

@Path(Paths.DONATE_EVENTS_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class DonateEventsResource {
	
	private final DonateEventsManager manager;
	
	@Inject
	public DonateEventsResource(DonateEventsManager manager) {
		this.manager = manager;
	}
	
	@POST
	public Donate createDonateEvent(@Auth User user, Donate donate) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to create a Donate Button Event", ApiDocs.EVENTS);
		}
		
		if (!donate.hasLinkText()) {
			throw new BadRequestException("You must provide link text to create a Donate Button", ApiDocs.EVENTS);
		}
		
		if (!donate.hasDescription()) {
			throw new BadRequestException("You must provide a description to create a Donate Event", ApiDocs.EVENTS);
		}
		
		return manager.createDonateEvent(donate);
	}

}
