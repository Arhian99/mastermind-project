package com.i2i.linkedinproject_backend.exceptionHandling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Description: Custom exception class thrown when no resource in the database matches the specifed identifier.
 * @see PlayerService#findByUsername @see GameService#getAndValidateSession()
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private String message;
		
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResourceNotFoundException(String message) {
		super(message);
		this.message=message;
	}
}
