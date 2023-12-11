package com.i2i.linkedinproject_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.ResourceNotFoundException;
import com.i2i.linkedinproject_backend.models.Player;
import com.i2i.linkedinproject_backend.repos.PlayerRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;

/*
 * Description: This is a service class which exposes various methods for interacating with the Player model and the Player repository
 * 
 * @author Arhian Albis Ramos
 */
@Service
public class PlayerService {
	@Autowired
	PlayerRepository playerRepo;
	
	/*
	 * Description: This method takes a player's username and queries the database @see PlayerRepository for a Player document that matches the specifed username.
	 * If there is not a player document in the database with the username passed in, it throws a resource not found exception.
	 * 
	 * @param username: username of player document that you want to retrieve
	 * @return Player object with specifed username or throws ResourceNotFouncException
	 */
	public Player findByUsername(String username) {
		return playerRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("No player found with specified username."));
	}
	
	/*
	 * Description: This method takes a username and password, it hashes the password using BCrypt algorithm @see @see BCrypt.withDefaults.hashToString() 
	 * @see https://github.com/patrickfav/bcrypt, and saves the newly created Player object (with a hashed password) to database @see PlayerRepository.
	 * Bcrypt implementation library credits to https://github.com/patrickfav/bcrypt
	 * 
	 * @param username: player's username
	 * @param password: player's password in plain text
	 * @return Player: newly created player object
	 */
	public Player save(String username, String password) {
		Player newPlayer = new Player(username, BCrypt.withDefaults().hashToString(12, password.toCharArray()));
		playerRepo.save(newPlayer);
		return  newPlayer;
	}
	
	/*
	 * Description: This method takes a username and password and first attempts to query the database to retrieve a Player object document that matches 
	 * the specified username. If unable to retrieve a Player object from the database with the specified username, it creates and saves a new Player object to
	 * the database @see save() method.
	 * 
	 * @param username: username of player
	 * @param: username: password of player
	 * @return: player with specified username and password.
	 */
	public Player findOrSave(String username, String password) {
		Player player = playerRepo.findByUsername(username).orElse(null);
		if(player == null) {
			player = save(username, password);
		}
		return player;
	}
}
