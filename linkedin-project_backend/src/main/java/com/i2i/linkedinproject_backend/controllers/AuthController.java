package com.i2i.linkedinproject_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.linkedinproject_backend.DTOs.FrontendDTO;
import com.i2i.linkedinproject_backend.DTOs.GameSessionDTO;
import com.i2i.linkedinproject_backend.DTOs.Principal;
import com.i2i.linkedinproject_backend.models.Player;
import com.i2i.linkedinproject_backend.services.AuthenticationService;
import com.i2i.linkedinproject_backend.services.GameService;
import com.i2i.linkedinproject_backend.services.PlayerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * Description: This is a controller class. All requests to /authenticate/** endpoints get mapped to this controller class and its methods. These requests are coming from 
 * from key entrypoints in the frontend application architecture. This class exposes endpoints for authenticating a user and/or saving a new user to the database, as well as
 * for retrieving a game session. The methods in this class expect Basic Authentication in the Authorization header of the request.
 * 
 * @author: Arhian Albis Ramos
 */
@RestController
@RequestMapping("/authenticate")
@CrossOrigin(origins="*")
public class AuthController {
	@Autowired
	PlayerService playerService;
	
	@Autowired
	GameService gameService;

	/*
	 * Description: This method handles POST request to /authenticate endpoint. It expects Basic Authentication format in the Authorization header of the request.
	 * It does not assume that the player already has an account @see PlayerService#findOrSave(String, String). This method expects Authorization header on the request with
	 * Basic Authentication. ie: "Authorization": "Basic username:password". It authenticates player @see AuthenticationService.authenticatePlayer() @see AuthenticationService.parseAuthHeader()
	 * 
	 *  @return the Player object with HttpStatus 200
	 */
	@PostMapping
	public ResponseEntity<?> handleLogin(HttpServletRequest req, HttpServletResponse resp){
		// principal refers to the the un-authenticated credentials recieved from the frontend.
		Principal principal = AuthenticationService.parseAuthHeader(req);

		Player player = playerService.findOrSave(principal.getUsername(), principal.getPassword());
		
		AuthenticationService.authenticatePlayer(player, principal);
		
		return ResponseEntity.ok(player);
	}
	

	/*
	 * Description: This method handles GET requests to /authenticate/continueSession?sessionID=<sessionID>&isMultiplayer=<isMultiplayer> endpoint. It assumes that for singleplayer sessions
	 * the Player already has an account, while for multiplayer sessions the player need not necessarily have an account @see PlayerService#findOrSave(). It authenticates players
	 * @see AuthenticationService.authenticatePlayer() and validates the session being requested @see GameService#getAndValidateSession().
	 * 
	 * @param sessionID: the sessionID of the session being requested
	 * @param isMultiplayer: whether or not the session being requested is singleplayer or multiplayer.
	 * 
	 * @return FrontendDTO: contains the GameSessionDTO requested and the Player who made the request.
	 */
	@GetMapping("/continueSession")
	public ResponseEntity<?> getSession(HttpServletRequest req, HttpServletResponse resp, 
			@RequestParam("sessionID") String sessionID, @RequestParam("isMultiplayer") boolean isMultiplayer) {
		
		// principal refers to the the un-authenticated credentials recieved from the frontend, @see Principal
		Principal principal = AuthenticationService.parseAuthHeader(req);
		Player player;
		
		if(isMultiplayer) {
			player = playerService.findOrSave(principal.getUsername(), principal.getPassword());
		} else {
			player = playerService.findByUsername(principal.getUsername());
		}
		
		AuthenticationService.authenticatePlayer(player, principal);
		
		GameSessionDTO gameSession = gameService.getAndValidateSession(sessionID, player.getUsername());
		
		return ResponseEntity.status(HttpStatus.OK).body(FrontendDTO.build(gameSession, player));
	}
}
