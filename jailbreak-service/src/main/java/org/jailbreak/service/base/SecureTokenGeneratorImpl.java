package org.jailbreak.service.base;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.jailbreak.service.core.SecureTokenGenerator;

public class SecureTokenGeneratorImpl implements SecureTokenGenerator {
	
	private static SecureRandom RANDOM;
	
	public SecureTokenGeneratorImpl() {
		// we only want to ever create one of these as 
		// they are very expensive to create
        if (RANDOM == null) {
            RANDOM = new SecureRandom();
        }
    }

	@Override
	public String nextToken() {
		return new BigInteger(260, RANDOM).toString(32);
	}

	
}
