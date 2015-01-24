package org.jailbreak.service.resources;

import io.dropwizard.auth.Auth;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.api.representations.Representations.User.UserLevel;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.errors.ForbiddenException;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.sun.jersey.api.NotFoundException;

@Path(Paths.CHECKINS_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CheckinsResource {
	
	private final CheckinsManager manager;
	
	@Inject
	public CheckinsResource(CheckinsManager manager) {
		this.manager = manager;
	}
	
	@GET
	public List<Checkin> getTeamCheckins(@PathParam("team_id") int team_id) {
        return this.manager.getTeamCheckins(team_id);
	}
	
	@POST
	public Checkin postCheckin(@Auth User user, Checkin checkin) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to create a checkin", ApiDocs.CHECKINS);
		}
		
		return this.manager.createCheckin(checkin);
	}
	
	@GET
	@Path("/{id}")
	public Checkin getCheckin(@PathParam("team_id") int team_id, @PathParam("id") int id) {
		Optional<Checkin> checkin = this.manager.getTeamCheckin(team_id, id);
		if (checkin.isPresent()) {
			return checkin.get();
		} else {
			throw new NotFoundException("No checkin with id " + id);
		}
	}
	
	@PUT
	@Path("/{id}")
	public Checkin updateCheckin(@Auth User user, @PathParam("id") int id, Checkin checkin) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to update a checkin", ApiDocs.CHECKINS);
		}
		
		Optional<Checkin> maybeCheckin = this.manager.updateCheckin(id, checkin);
		if (maybeCheckin.isPresent()) {
			return maybeCheckin.get();
		} else {
			throw new NotFoundException("No checking with id " + id);
		}
	}
	
}