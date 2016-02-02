package org.jailbreak.service.resources;

import io.dropwizard.auth.Auth;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.api.representations.Representations.User.UserLevel;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.errors.BadRequestException;
import org.jailbreak.service.errors.ForbiddenException;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

@Path(Paths.CHECKINS_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CheckinsResource {
	
	@Context
	private UriInfo uriInfo;
	
	private final CheckinsManager manager;
	private final ResourcesHelper helper;
	
	@Inject
	public CheckinsResource(CheckinsManager manager,
			ResourcesHelper helper) {
		this.manager = manager;
		this.helper = helper;
	}
	
	@GET
	public List<Checkin> getTeamCheckins(@PathParam("team_id") int team_id) {
        return response(manager.getTeamCheckins(team_id));
	}
	
	@POST
	public Checkin postCheckin(@Auth User user, @PathParam("team_id") int teamId, @BeanParam Checkin checkin) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to create a checkin", ApiDocs.CHECKINS);
		}
		
		if (checkin.hasTeamId() && teamId != checkin.getTeamId()) {
			throw new BadRequestException("The team id in the path must match the one in the URL", ApiDocs.CHECKINS);
		} else {
			checkin = checkin.toBuilder().setTeamId(teamId).build();
		}
		
		if (!checkin.hasLat()) {
			throw new BadRequestException("You must provide a latitude value to create this Checkin", ApiDocs.CHECKINS);
		}
		
		if (!checkin.hasLon()) {
			throw new BadRequestException("You must provide a longitude value to create this Checkin", ApiDocs.CHECKINS);
		}
		
		return response(manager.createCheckin(checkin));
	}
	
	@GET
	@Path("/{id}")
	public Optional<Checkin> getCheckin(@PathParam("team_id") int teamId, @PathParam("id") int id) {
		return response(manager.getTeamCheckin(teamId, id));
	}
	
	@PUT
	@Path("/{id}")
	public Optional<Checkin> updateCheckin(@Auth User user, @PathParam("id") int id, @BeanParam Checkin checkin) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to update a checkin", ApiDocs.CHECKINS);
		}
		
		if (id != checkin.getId()) {
			throw new BadRequestException("Checkin id in request body must match checkin id in the url", ApiDocs.CHECKINS_UPDATE);
		}
		
		return response(manager.updateCheckin(checkin));
	}
	
	// response builders
	private List<Checkin> response(List<Checkin> checkins) {
		List<Checkin> results = Lists.newArrayListWithCapacity(checkins.size());
		for (Checkin checkin : checkins) {
			Checkin.Builder builder = checkin.toBuilder();
			builder.setHref(helper.buildUrl(uriInfo, UriBuilder.fromUri(Paths.CHECKINS_PATH).build(checkin.getTeamId()).toString(), checkin.getId()));
			results.add(builder.build());
		}
		return results;
	}
	
	private Optional<Checkin> response(Optional<Checkin> maybe) {
		if (maybe.isPresent()) {
			return Optional.of(response(Lists.newArrayList(maybe.get())).get(0));
		} else {
			return Optional.absent();
		}
	}
	
	private Checkin response(Checkin checkin) {
		return response(Lists.newArrayList(checkin)).get(0);
	}
	
}