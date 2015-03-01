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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.TeamsFilters;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.api.representations.Representations.User.UserLevel;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.errors.BadRequestException;
import org.jailbreak.service.errors.ForbiddenException;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@Path("/teams")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeamsResource {
	
	private final TeamsManager manager;
	private final int defaultLimit;
	private final int maxLimit;
	
	@Inject
	public TeamsResource(TeamsManager manager,
			@Named("resources.defaultLimit") int defaultLimit,
			@Named("resources.maxLimit") int maxLimit) {
		this.manager = manager;
		this.defaultLimit = defaultLimit;
		this.maxLimit = maxLimit;
	}
	
	@GET
	@Timed
	public List<Team> getTeams(@QueryParam("limit") Optional<Integer> maybeLimit,
			@QueryParam("filters") Optional<String> maybeFilters) {
		int limit = ResourcesHelper.limit(maybeLimit, defaultLimit, maxLimit);
		TeamsFilters filters = ResourcesHelper.decodeUrlEncodedJson(maybeFilters, TeamsFilters.class, TeamsFilters.newBuilder().build(), ApiDocs.TEAMS_FILTERS);
        
		return this.manager.getTeams(limit, filters);
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
	
	@GET
	@Path("/slug/{slug}")
	public Optional<Team> getTeamSlug(@PathParam("slug") String slug) {
		return manager.getTeamSlug(slug);
	}
	
	@PUT
	@Path("/{id}")
	public Optional<Team> putTeam(@Auth User user, @PathParam("id") int id, Team team) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to update a team", ApiDocs.TEAMS);
		}
		
		if (id != team.getId()) {
			throw new BadRequestException("Team id in request body must match team id in path", ApiDocs.TEAMS_UPDATES);
		}
		
		if (team.hasLastCheckin()) {
			// cannot update last checkin field - ignore it on PUT requests
			team = team.toBuilder().clearLastCheckin().build();
		}
		
		return this.manager.updateTeam(team);
	}
	
	@PATCH
	@Path("/{id}")
	public Optional<Team> patchTeam(@Auth User user, @PathParam("id") int id, Team team) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to update a team", ApiDocs.TEAMS);
		}
		
		if (!team.hasId()) {
			team = team.toBuilder().setId(id).build();
		}
		
		if (id != team.getId()) {
			throw new BadRequestException("Team id in request body must match team id in path", ApiDocs.TEAMS_UPDATES);
		}
		
		if (team.hasLastCheckin()) {
			// throw error instead of ignoring because clients should send the minimum number of fields on PATCH requests
			throw new BadRequestException("You cannot update the last checkin field on team. You can only create new checkins", ApiDocs.TEAMS_UPDATES);
		}
		
		return this.manager.patchTeam(team);
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