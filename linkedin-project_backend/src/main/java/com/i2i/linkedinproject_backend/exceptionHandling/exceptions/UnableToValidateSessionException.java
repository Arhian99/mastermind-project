package com.i2i.linkedinproject_backend.exceptionHandling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Custom exception class thrown when the player attempting to access a GameSession does not have permissions to access the session @see GameService#getAndValidateSession()
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnableToValidateSessionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String message;
		
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UnableToValidateSessionException(String message) {
		super(message);
		this.message=message;
	}
}
