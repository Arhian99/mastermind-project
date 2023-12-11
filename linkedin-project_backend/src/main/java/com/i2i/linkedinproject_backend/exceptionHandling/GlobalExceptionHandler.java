package com.i2i.linkedinproject_backend.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.AlreadyExistsException;
import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.ExternalAPIException;
import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.IncorrectPasswordException;
import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.InvalidInputException;
import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.ResourceNotFoundException;
import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.UnableToValidateSessionException;

/*
 * Description: Controller advice class which catches all custom exceptions thrown wihtin the application and returns an appropiate response to the frontend.
 * The body of the responses returned are the messages from the exceptions.
 * 
 * @author Arhian Albis Ramos
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<?> handleInvalidInputException(InvalidInputException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<?> handleAlreadyExistsException(AlreadyExistsException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(ExternalAPIException.class)
	public ResponseEntity<?> handleExternalAPIException(ExternalAPIException e){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
	
	@ExceptionHandler(UnableToValidateSessionException.class)
	public ResponseEntity<?> handleUnableToValidateSessionException(UnableToValidateSessionException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(IncorrectPasswordException.class)
	public ResponseEntity<?> handleIncorrectPasswordException(IncorrectPasswordException e){
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}
}
