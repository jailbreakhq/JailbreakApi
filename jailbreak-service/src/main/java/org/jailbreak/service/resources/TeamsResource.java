package org.jailbreak.service.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.PATCH;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
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
import com.google.common.collect.Lists;
import com.google.inject.Inject;

@Path("/teams")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeamsResource {
	
	@Context
	private UriInfo uriInfo;
	
	private final TeamsManager manager;
	private final ResourcesHelper helper;
	
	@Inject
	public TeamsResource(TeamsManager manager,
			ResourcesHelper helper) {
		this.manager = manager;
		this.helper = helper;
	}
	
	@GET
	@Timed
	public Response getTeams(@QueryParam("limit") Optional<Integer> maybeLimit,
			@QueryParam("page") Optional<Integer> page,
			@QueryParam("filters") Optional<String> maybeFilters) {
		int limit = helper.limit(maybeLimit);
		TeamsFilters filters = helper.decodeUrlEncodedJson(maybeFilters, TeamsFilters.class, TeamsFilters.newBuilder().build(), ApiDocs.TEAMS_FILTERS);
        
		List<Team> teams = this.manager.getTeams(limit, page, filters);
		int totalCount = this.manager.getTotalCount(filters);
		
		return response(teams, totalCount);
	}
	
	@Path("/all")
	@GET
	public List<Team> getAllTeams() {
		return this.response(this.manager.getAllTeams());
	}
	
	@Path("/lastcheckin")
	@GET
	public List<Team> getTeamsByLastCheckin() {
		return this.response(this.manager.getTeamsByLastCheckin());
	}
	
	@POST
	public Team addTeam(@Auth User user, @BeanParam Team team) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to update a team", ApiDocs.TEAMS);
		}
		
		return response(manager.addTeam(team));
	}
	
	@GET
	@Path("/{id:\\d+}")
	public Optional<Team> getTeam(@PathParam("id") int id) {
		return response(manager.getTeam(id));
	}
	
	@GET
	@Path("/{slug:[a-zA-Z][a-zA-Z0-9\\-]+}")
	public Optional<Team> getTeamSlug(@PathParam("slug") String slug) {
		return response(manager.getTeamSlug(slug));
	}
	
	@PUT
	@Path("/{id}")
	public Optional<Team> putTeam(@Auth User user, @PathParam("id") int id, @BeanParam Team team) {
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
		
		return response(manager.updateTeam(team));
	}
	
	@PATCH
	@Path("/{id}")
	public Optional<Team> patchTeam(@Auth User user, @PathParam("id") int id, @BeanParam Team team) {
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
		
		return response(manager.patchTeam(team));
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
	
	// Response Builder Methods
	private List<Team> response(List<Team> teams) {
		List<Team> results = Lists.newArrayListWithCapacity(teams.size());
		for (Team team : teams) {
			Team.Builder builder = team.toBuilder();

			builder.setHref(helper.buildUrl(uriInfo, Paths.TEAMS_PATH, team.getId()));
			builder.setCheckinsUrl(helper.buildUrl(uriInfo, UriBuilder.fromUri(Paths.CHECKINS_PATH).build(team.getId()).toString()));
			
			results.add(builder.build());
		}
		return results;
	}
	
	private Response response(List<Team> teams, int totalCount) {
		List<Team> results = response(teams);
		return Response.ok(results).header(Headers.X_TOTAL_COUNT, totalCount).build();
	}
	
	private Optional<Team> response(Optional<Team> donation) {
		if (donation.isPresent()) {
			return Optional.of(response(Lists.newArrayList(donation.get())).get(0));
		} else {
			return Optional.absent();
		}
	}
	
	private Team response(Team team) {
		return response(Lists.newArrayList(team)).get(0);
	}
}