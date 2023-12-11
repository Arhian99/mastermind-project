package com.i2i.linkedinproject_backend.exceptionHandling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Description: Exception class thrown when a user tries to save a resource that already exists in the database. ie: If a GameSession is already associated with a certain 
 * sessionID and the user tries to save a new session with the same sessionID.
 * 
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyExistsException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public AlreadyExistsException(String message) {
		super();
		this.message = message;
	}
}
