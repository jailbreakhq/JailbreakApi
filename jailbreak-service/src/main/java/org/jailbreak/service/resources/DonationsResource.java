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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Donation.DonationsFilters;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.api.representations.Representations.User.UserLevel;
import org.jailbreak.service.core.DonationsManager;
import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.errors.BadRequestException;
import org.jailbreak.service.errors.ForbiddenException;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@Path(Paths.DONATIONS_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DonationsResource {
	
	private final DonationsManager manager;
	private final int defaultLimit;
	private final int maxLimit;
	
	@Inject
	public DonationsResource(DonationsManager manager, 
			@Named("resources.defaultLimit") int defaultLimit,
			@Named("resources.maxLimit") int maxLimit) {
		this.manager = manager;
		this.defaultLimit = defaultLimit;
		this.maxLimit = maxLimit;
	}
	
	@GET
	public Response getDonations(@QueryParam("limit") Optional<Integer> maybeLimit,
			@QueryParam("filters") Optional<String> maybeFilters) {
		Integer limit = ResourcesHelper.limit(maybeLimit, defaultLimit, maxLimit);
		
		DonationsFilters filters;
		if (maybeFilters.isPresent()) {
			filters = ResourcesHelper.decodeUrlEncodedJson(maybeFilters.get(), DonationsFilters.class);
		} else {
			filters = DonationsFilters.newBuilder().build();
		}
		
		List<Donation> donations = this.manager.getDonations(limit, filters);
		int totalCount = this.manager.getTotalCount(filters);
		
		donations = this.manager.filterPrivateFields(donations);
		
		return Response.ok(donations).header(Headers.X_TOTAL_COUNT, totalCount).build();
	}
	
	@POST
	public Donation postDonation(@Auth User user, Donation donation) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to create a donation", ApiDocs.DONATIONS);
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