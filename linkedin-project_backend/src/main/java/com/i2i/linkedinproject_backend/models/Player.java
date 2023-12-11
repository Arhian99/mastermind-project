package com.i2i.linkedinproject_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * Description: This class represents a Player model which in turn represents a player in the game. Player objects are persisted as documents in the database
 * in the "players" collection. 
 * 
 * @author Arhian Albis Ramos
 */
@Document("players")
public class Player {
	@Id
	private String id;				// unique identifier set by MongoDB databse instance, used internally by mongoDB.
	private String username;		// user-defined player username.
	private String password;		// user-defined player password.

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Player() {
		this.username ="";
		this.password="";
	}
	
	public Player(String username, String password) {
		this.username=username;
		this.password=password;
	}

	public String toString() {
		return "Player [username=" + username + "]";
	}
}
