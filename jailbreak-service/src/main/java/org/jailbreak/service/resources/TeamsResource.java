package org.jailbreak.service.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.PATCH;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.api.representations.Representations.User.UserLevel;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.errors.ForbiddenException;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/teams")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeamsResource {
	
	private final TeamsManager manager;
	
	@Inject
	public TeamsResource(TeamsManager manager) {
		this.manager = manager;
	}
	
	@GET
	@Timed
	public List<Team> getTeams() {
        return this.manager.getTeams();
	}
	
	@POST
	public Team addTeam(@Auth User user, Team team) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to update a team", ApiDocs.TEAMS);
		}
		
		return this.manager.addTeam(team);
	}
	
	@GET
	@Path("/{id}")
	public Optional<Team> getTeam(@PathParam("id") int id) {
		return manager.getTeam(id);
	}
	
	@PUT
	@Path("/{id}")
	public Optional<Team> putTeam(@Auth User user, @PathParam("id") int id, Team team) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to update a team", ApiDocs.TEAMS);
		}
		
		return this.manager.updateTeam(id, team);
	}
	
	@PATCH
	@Path("/{id}")
	public Optional<Team> patchTeam(@Auth User user, @PathParam("id") int id, Team team) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to update a team", ApiDocs.TEAMS);
		}
		
		return this.manager.patchTeam(id, team);
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteTeam(@Auth User user, @PathParam("id") int id) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary priveledges to delete a team", ApiDocs.TEAMS);
		}
		
		this.manager.deleteTeam(id);
		return Response.status(Status.NO_CONTENT).build();
	}
}