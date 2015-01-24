package org.jailbreak.service.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.PATCH;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.service.core.TeamsManager;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.sun.jersey.api.NotFoundException;

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
		return this.manager.addTeam(team);
	}
	
	@GET
	@Path("/{id}")
	public Team getTeam(@PathParam("id") int id) {
		Optional<Team> team = manager.getTeam(id);
		if (team.isPresent()) {
			return team.get();
		} else {
			throw new NotFoundException("No team with id " + id);
		}
	}
	
	@PUT
	@Path("/{id}")
	public Optional<Team> putTeam(@Auth User user, @PathParam("id") int id, Team team) {
		return this.manager.updateTeam(id, team);
	}
	
	@PATCH
	@Path("/{id}")
	public Optional<Team> patchTeam(@Auth User user, @PathParam("id") int id, Team team) {
		return this.manager.patchTeam(id, team);
	}
}