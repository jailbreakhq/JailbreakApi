package org.jailbreak.service.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.service.core.CheckinsManager;

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
	
	@GET
	@Path("/{id}")
	public Checkin getCheckin(@PathParam("team_id") int team_id, @PathParam("id") int id) {
		Optional<Checkin> checkin = manager.getCheckin(team_id, id);
		if (checkin.isPresent()) {
			return checkin.get();
		} else {
			throw new NotFoundException("No checkin with id " + id);
		}
	}
}