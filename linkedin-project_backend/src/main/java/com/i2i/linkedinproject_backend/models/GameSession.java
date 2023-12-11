package com.i2i.linkedinproject_backend.models;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * Description: This class represents a Game Session and all its associated data. It is persisted as a Document in the database @see GameSessionRepository.
 * In the "sessions" collection. 
 * 
 * @author Arhian Albis Ramos 
 */
@Document("sessions")
public class GameSession {
	@Id
	private String id;						// unique identifier set by MongoDB database instance. Used internally by MongoDB.
	private String sessionID;				// unique user-defined identifier for the game session
	private boolean isMultiplayer;			// defines whether game session is a multiplayer or a singleplayer game session
	private String codebreaker;				// stores the username of the codebreaker in the game session
	private String codemaker;				// stores the username of the codemaker in the game session. It is always null in singleplayer sessions
	private int guessNumb;					// stores the current number of guesses that have been made by the code breaker
	private Guess currentGuess;				// stores the Guess object which represents the latest guess made by the codebreaker
	private ArrayList<Guess> guesses;		// array list of all Guess objects that have been made by the codebreaker in this session. 
	private GameSettings gameSettings;		// stores GameSettings object associated with this game sesison
	private int roundNumb;					// stores the current round number. 
	private boolean isRoundOver;			// stores whether or not the current round has ended.
	private boolean isGameOver;				// stores whether or not this game session has ended
	private String winner;					// stores the winner for this game session.  It is null while game session is NOT over.
	private String secret;					// stores the secret for this game session. 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public boolean isMultiplayer() {
		return isMultiplayer;
	}

	public void setMultiplayer(boolean isMultiplayer) {
		this.isMultiplayer = isMultiplayer;
	}

	public String getCodebreaker() {
		return codebreaker;
	}

	public void setCodebreaker(String codebreaker) {
		this.codebreaker = codebreaker;
	}

	public String getCodemaker() {
		return codemaker;
	}

	public void setCodemaker(String codemaker) {
		this.codemaker = codemaker;
	}

	public int getGuessNumb() {
		return guessNumb;
	}

	public void setGuessNumb(int guessNumb) {
		this.guessNumb = guessNumb;
	}

	public Guess getCurrentGuess() {
		return currentGuess;
	}

	public void setCurrentGuess(Guess currentGuess) {
		this.currentGuess = currentGuess;
	}

	public ArrayList<Guess> getGuesses() {
		return guesses;
	}

	public void setGuesses(ArrayList<Guess> guesses) {
		this.guesses = guesses;
	}

	public GameSettings getGameSettings() {
		return gameSettings;
	}

	public void setGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

	public int getRoundNumb() {
		return roundNumb;
	}

	public void setRoundNumb(int roundNumb) {
		this.roundNumb = roundNumb;
	}

	public boolean isRoundOver() {
		return isRoundOver;
	}

	public void setRoundOver(boolean isRoundOver) {
		this.isRoundOver = isRoundOver;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public GameSession(String sessionID, boolean isMultiplayer, String codebreaker, String codemaker, int guessNumb,
			Guess currentGuess, ArrayList<Guess> guesses, GameSettings gameSettings, int roundNumb, boolean isRoundOver,
			boolean isGameOver, String winner, String secret) {
		
		this.sessionID = sessionID;
		this.isMultiplayer = isMultiplayer;
		this.codebreaker = codebreaker;
		this.codemaker = codemaker;
		this.guessNumb = guessNumb;
		this.currentGuess = currentGuess;
		this.guesses = guesses;
		this.gameSettings = gameSettings;
		this.roundNumb = roundNumb;
		this.isRoundOver = isRoundOver;
		this.isGameOver = isGameOver;
		this.winner = winner;
		this.secret = secret;
	}

	public GameSession() {
	}
}
