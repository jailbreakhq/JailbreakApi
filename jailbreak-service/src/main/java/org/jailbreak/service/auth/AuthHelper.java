package org.jailbreak.service.auth;

public class AuthHelper {
	
	public static boolean areEqualConstantTime(String a, String b) {
		boolean equal = true;
	    if (a.length() != b.length()) {
	       equal = false;
	    }

	    for (int i = 0; i < a.length(); i++) {
	        if (a.charAt(i%a.length()) != b.charAt(i%b.length())) {
	            equal = false;
	        }
	    }
	    return equal;
	}

}
