package org.jailbreak.service.errors;

@SuppressWarnings("serial")
public class BaseException extends RuntimeException {
	
	private int status;
	private String message;
	private String docs_link;
	
	public BaseException(int status, String message, String docs_link) {
		this.status = status;
		this.message = message;
		this.docs_link = docs_link;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDocsLink() {
		return docs_link;
	}

	public void setDocsLink(String docs_link) {
		this.docs_link = docs_link;
	}

}
