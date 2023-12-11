package com.i2i.linkedinproject_backend.exceptionHandling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Description: Exception class thrown when the principal password does not match the saved password for the specifed username. Thrown when unable 
 * to autheticate user. @see AuthenticationService
 * 
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class IncorrectPasswordException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String message;
		
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public IncorrectPasswordException(String message) {
		super(message);
		this.message=message;
	}
}
