package org.jailbreak.service.core;

import org.jailbreak.api.representations.Representations.StripeChargeRequest;

import com.stripe.exception.StripeException;

public interface StripeManager {

	public boolean chargeCard(StripeChargeRequest request) throws StripeException;
	
}
