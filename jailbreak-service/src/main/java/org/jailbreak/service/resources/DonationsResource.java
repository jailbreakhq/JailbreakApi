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
import org.jailbreak.service.auth.AuthHelper;
import org.jailbreak.service.core.DonationsManager;
import org.jailbreak.service.errors.ApiDocs;
import org.jailbreak.service.errors.BadRequestException;
import org.jailbreak.service.errors.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@Path(Paths.DONATIONS_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DonationsResource {
	
	private final DonationsManager manager;
	private final String donationsWebhookSecret;
	private final Logger LOG = LoggerFactory.getLogger(DonationsResource.class);
	
	@Inject
	public DonationsResource(DonationsManager manager, @Named("stripe.webhook.secret") String donationsWebhookSecret) {
		this.manager = manager;
		this.donationsWebhookSecret = donationsWebhookSecret;
	}
	
	@GET
	public List<Donation> getDonation() {
        return this.manager.getDonations();
	}
	
	@POST
	public Donation postDonation(@Auth User user, Donation donation) {
		if (user.getUserLevel() != UserLevel.SUPERADMIN) {
			throw new ForbiddenException("You don't have the necessary permissions to create a donation", ApiDocs.DONATIONS);
		}
		
		return this.manager.createDonation(donation);
	}
	
	@POST
	@Path("/{secret}")
	public Donation donationsWebhook(@PathParam("secret") String pathSecret, Donation donation) {
		String error = "You don't have the correct auth token to access this resource";
		if (donationsWebhookSecret.isEmpty()) {
			LOG.error("The donations webhook secret config value is not set. Refusing all connections");
			throw new ForbiddenException(error, ApiDocs.DONATIONS_WEBHOOK);
		}
		
		if (AuthHelper.areEqualConstantTime(pathSecret, donationsWebhookSecret)) {
			throw new ForbiddenException(error, ApiDocs.DONATIONS_WEBHOOK);
		}
		
		LOG.info("Recieved and processing donations webhook");
		
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