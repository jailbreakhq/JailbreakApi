package org.jailbreak.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.jailbreak.service.helpers.PasswordHashingHelper;

public class Password {
	
	public static void main(String args[]) throws NoSuchAlgorithmException, InvalidKeySpecException {
		System.out.println(PasswordHashingHelper.createHash("enU0hL%LKO#J4Ivz"));
	}

}
