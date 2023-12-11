package com.i2i.linkedinproject_backend.exceptionHandling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Description: Custom exception class thrown when user input does not match expected input. ie: guess or secret are in the incorrect formats.
 * @see GameService#validateGuess() and GameService#validateSecret()
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public InvalidInputException(String message) {
		super();
		this.message = message;
	}
}
