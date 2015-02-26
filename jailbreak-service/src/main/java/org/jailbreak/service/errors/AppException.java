package org.jailbreak.service.errors;

@SuppressWarnings("serial")
public class AppException extends BaseException {

	private final static int CODE = 500;
	private Exception e;
	
	public AppException(String message, Exception e) {
		super(CODE, message, null);
		
		this.e = e;
	}

	public Exception getOriginalException() {
		return e;
	}

}
