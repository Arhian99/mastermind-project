package com.i2i.linkedinproject_backend.exceptionHandling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Description: Exception class throw when theres a problem with the Random Number API (https://www.random.org/integers/**) @see RandomNumberService
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExternalAPIException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ExternalAPIException(String message) {
		super();
		this.message = message;
	}
}
