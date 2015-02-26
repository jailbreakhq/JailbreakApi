package org.jailbreak.service.base;

import java.util.Map;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Donation.DonationType;
import org.jailbreak.api.representations.Representations.StripeChargeRequest;
import org.jailbreak.service.core.DonationsManager;
import org.jailbreak.service.core.StripeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public class StripeManagerImpl implements StripeManager {

	private final DonationsManager donations;
	private final Logger LOG = LoggerFactory.getLogger(StripeManagerImpl.class);
	
	@Inject
	public StripeManagerImpl(DonationsManager donations,
			@Named("stripe.secret.key") String key) {
		this.donations = donations;
		Stripe.apiKey = key;
	}
	
	@Override
	public boolean chargeCard(StripeChargeRequest request) throws StripeException {
		String from_source;
		if (request.hasName()) {
			from_source = request.getName();
		} else if (request.hasEmail()) {
			from_source = request.getEmail();
		} else {
			from_source = "donator";
		}
		
		Map<String, Object> chargeParams = Maps.newHashMap();
		chargeParams.put("amount", request.getAmount());
		chargeParams.put("currency", "EUR");
		chargeParams.put("card", request.getToken());
		chargeParams.put("description", "Online donation from " + from_source + ". Thank you!");
		chargeParams.put("statement_descriptor", "JailbreakHQ donation");
		
		if (request.hasEmail()) {
			chargeParams.put("receipt_email", request.getEmail());
		}
		
		LOG.info("Sending charge request to stripe of amount " + request.getAmount()/100 + " euro");
		Charge.create(chargeParams);
		LOG.info("Request charge to stripe successful");
		
		boolean result = true; // the donation was processed ensure no errors after this point make the user think the donation failed
		
		// create a donation record since the Charge.create didn't spew exceptions
		try {
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
			
			Donation donation = builder.build();
			donations.createDonation(donation);
		} catch (Exception e) {
			// bury exception if any in the logs - the stripe charge was successful so we want to return a positive result
			LOG.error("Error handling donation post charge actions: " + e.getMessage());
		}
		
		return result;
	}

}
