package org.jailbreak.service.base;

import java.util.Map;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Donation.DonationType;
import org.jailbreak.api.representations.Representations.StripeChargeRequest;
import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.service.core.DonationsManager;
import org.jailbreak.service.core.StripeManager;
import org.jailbreak.service.core.TeamsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public class StripeManagerImpl implements StripeManager {

	private final DonationsManager donations;
	private final TeamsManager teams;
	private final Logger LOG = LoggerFactory.getLogger(StripeManagerImpl.class);
	
	@Inject
	public StripeManagerImpl(DonationsManager donations,
			TeamsManager teams,
			@Named("stripe.secret.key") String key) {
		this.donations = donations;
		this.teams = teams;
		Stripe.apiKey = key;
	}
	
	@Override
	public boolean chargeCard(StripeChargeRequest request) throws StripeException {
		Map<String, Object> chargeParams = Maps.newHashMap();
		chargeParams.put("amount", request.getAmount());
		chargeParams.put("currency", "EUR");
		chargeParams.put("card", request.getToken());
		chargeParams.put("description", "Online donation from " + request.getName());
		chargeParams.put("statement_descriptor", "JailbreakHQ donation");
		
		LOG.info("Sending charge request to stripe of amount " + request.getAmount()/100 + " euro");
		Charge.create(chargeParams);
		LOG.info("Request charge to stripe successful");
		
		boolean result = true; // the donation was processed ensure no erros after this point make the user think the donation failed
		
		// create a donation record since the Charge.create didn't spew exceptions
		Donation.Builder builder = Donation.newBuilder()
				.setTeamId(request.getTeamId())
				.setAmount(request.getAmount())
				.setEmail(request.getEmail())
				.setType(DonationType.ONLINE);
		
		if (request.hasName() && !request.getName().isEmpty()) {
			builder.setName(request.getName());
		} else {
			builder.setName("Anonymous");
		}
		
		if (!request.getBacker()) {
			builder.setName("Anonymous");
		}
		
		// update the count on the teams object
		if (request.hasTeamId()) {
			Optional<Team> maybeTeam = teams.getTeam(request.getTeamId());
			if (maybeTeam.isPresent()) {
				Team team = maybeTeam.get();
				team = team.toBuilder()
				.setAmountRaisedOnline(team.getAmountRaisedOnline() + request.getAmount())
				.build();
				teams.updateTeam(team);
			}
		}
		
		Donation donation = builder.build();
		donations.createDonation(donation);
		
		return result;
	}

}
