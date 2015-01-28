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

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.api.representations.Representations.User.UserLevel;
import org.jailbreak.service.core.DonationsManager;
import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.errors.BadRequestException;
import org.jailbreak.service.errors.ForbiddenException;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path(Paths.DONATIONS_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DonationsResource {
	
	private final DonationsManager manager;
	
	@Inject
	public DonationsResource(DonationsManager manager) {
		this.manager = manager;
	}
	
	@GET
	public List<Donation> getDonation() {
        return this.manager.getDonations();
	}
	
	@POST
	public Donation postDonation(@Auth User user, Donation donation) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to create a checkin", ApiDocs.DONATIONS);
		}
		
		return this.manager.createDonation(donation);
	}
	
	@GET
	@Path("/{id}")
	public Optional<Donation> getDonation(@PathParam("id") int id) {
		return this.manager.getDonation(id);
	}
	
	@PUT
	@Path("/{id}")
	public Optional<Donation> updateDonation(@Auth User user, @PathParam("id") int id, Donation donation) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to update a donation", ApiDocs.DONATIONS);
		}
		
		if (id != donation.getId()) {
			throw new BadRequestException("The donation id in the request body must match the id in the URL", ApiDocs.DONATIONS);
		}
		
		boolean result = this.manager.updateDonation(donation);
		if(result)
			return Optional.of(donation);
		else
			return Optional.absent();
	}
	
}