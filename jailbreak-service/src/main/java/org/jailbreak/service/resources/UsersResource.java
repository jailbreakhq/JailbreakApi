package org.jailbreak.service.resources;

import io.dropwizard.auth.Auth;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.api.representations.Representations.User.UserLevel;
import org.jailbreak.auth.errors.ApiDocs;
import org.jailbreak.errors.common.ForbiddenException;
import org.jailbreak.service.core.UsersManager;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.sun.jersey.api.NotFoundException;

@Path(Paths.USERS_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
	
	private final UsersManager manager;
	
	@Inject UsersResource(UsersManager manager) {
		this.manager = manager;
	}
	
	@GET
	public List<User> getUsers(@Auth User user) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to view all users", ApiDocs.USERS);
		}
		
		return this.manager.getUsers();
	}
	
	@Path("/{id}")
	@GET
	public User getUser(@Auth User user, @PathParam("id") long user_id) {
		if (user.getUserId() != user_id || user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You do not have the necessary permissions to view user with id " + user_id, ApiDocs.USERS);
		}
		
		Optional<User> object = this.manager.getUser(user_id);
		if (object.isPresent()) {
			return object.get();
		} else {
			throw new NotFoundException("No User with id " + user_id);
		}
	}
	
}
