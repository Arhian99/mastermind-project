package com.i2i.linkedinproject_backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.i2i.linkedinproject_backend.DTOs.Principal;
import com.i2i.linkedinproject_backend.controllers.GameController;
import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.IncorrectPasswordException;
import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.ResourceNotFoundException;
import com.i2i.linkedinproject_backend.models.Player;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * Description: Auhtentication Service class which exposes various methods to authenticate a Principal agianst a Player stored in the database. 
 * 
 * @author Arhian Albis Ramos
 */
@Service
public class AuthenticationService {
	private static final Logger logger = LoggerFactory.getLogger(GameController.class);
	
	/*
	 * Description: This method parses an Authorization header from an HttpServletRequest object and returns a Principal object containing the credentials. It assumes
	 * Basic Authentication.
	 * 
	 * @param HttpServletRequest: request object contianing an Auhtorization header with Basic Authentication
	 * @return Principal: object with the credentials extracted from the Authorization header.
	 */
	public static Principal parseAuthHeader(HttpServletRequest req) {
		String authHeader = req.getHeader("Authorization");   			// Basic <username>:<password>
		return Principal.buildFromBasicAuth(authHeader);
	}
	
	/*
	 * Description: This method authenticates the passed in principal against the passed in player object. It assumes the principal already contains a hashed password
	 * and NOT a password in plain text therefore compares the usename and password directly without first hashing the principal's password. 
	 * 
	 *  @param HttpServletRequest: the request from which the principal was extracted 
	 *  @param HttpServletResponse: the response that will be sent associated with this request
	 *  @param Player: the player object to which the principal claims (principal is authenticated agianst this Player object)
	 *  @param Principal: credentials which require authentication
	 */
	public static void authenticateActivePlayer(HttpServletRequest req, HttpServletResponse resp, Player player, Principal principal) {
		if(player.getUsername().equals(principal.getUsername())) {
			if(player.getPassword().equals(principal.getPassword())) {
				// user authentication success
				logger.info("Authentication success: "+player.getUsername());
				
			} else {
				// passwords do not match
				throw new IncorrectPasswordException("Password entered is incorrect for specified username");
			}
		} 

		else throw new ResourceNotFoundException("Unable to authenticate player with username: "+principal.getUsername());
	}
	
	
	/*
	 * Description: This method takes a Player object and Principal object and attempts to authenticate the principal credentials against the player credentials.
	 * The Player is coming from the database @see AuthController the principal are the credentials coming from the frontend. It assumes the principal credentials
	 * are in plain text. Therefore it hashses the principal password (in accordance to patrickfav BCrypt implementation) before comparing them. 
	 * @see BCrypt.verifier.verify() @see https://github.com/patrickfav/bcrypt
	 * Bcrypt implementation library credits to https://github.com/patrickfav/bcrypt
	 * 
	 * @param Player credentials which principal claims (used to authenticate principal against these credentials)
	 * @param Principal credentials coming from the frontend which claims access to Player. This method assumes these credentials are in plain text.
	 * 
	 */
	public static void authenticatePlayer(Player player, Principal principal){
		
		if(player.getUsername().equals(principal.getUsername())) {
			
			// Using patrickfav bcrypt implementation java library @see https://github.com/patrickfav/bcrypt
			BCrypt.Result result = BCrypt.verifyer().verify(principal.getPassword().toCharArray(), player.getPassword());
			
			if(result.verified) {
				// user auhentication success
				logger.info("Authentication success: "+player.getUsername());
				
			} else {
				// passwords do not match
				throw new IncorrectPasswordException("Password entered is incorrect for specified username");
			}
		} 
		
		else throw new ResourceNotFoundException("Unable to authenticate player with username: "+principal.getUsername());
	}
	
	/*
	 * Description: Overloaded version of above method whihc includes the associated HttpServletRequest and HttpServletResponse for further actions upon 
	 * authentication or upo authentication failure.
	 */
	public static void authenticatePlayer(HttpServletRequest req, HttpServletResponse resp, Player player, Principal principal){
		
		if(player.getUsername().equals(principal.getUsername())) {
			
			BCrypt.Result result = BCrypt.verifyer().verify(principal.getPassword().toCharArray(), player.getPassword());
			
			if(result.verified) {
				// user auhentication success
				logger.info("Authentication success: "+player.getUsername());
				
				// do stuff with request and response (future use)
				
			} else {
				// passwords do not match
				throw new IncorrectPasswordException("Password entered is incorrect for specified username");
				
				// do stuff with request and response (future use)
			}
		} 
		
		else throw new ResourceNotFoundException("Unable to authenticate player with username: "+principal.getUsername());
	}


}
