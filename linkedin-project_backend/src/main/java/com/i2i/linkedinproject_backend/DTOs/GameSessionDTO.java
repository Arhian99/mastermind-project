package com.i2i.linkedinproject_backend.DTOs;

import java.util.ArrayList;

import com.i2i.linkedinproject_backend.models.GameSession;
import com.i2i.linkedinproject_backend.models.Guess;

/*
 * Description: This class serves as a representation of the GameSession Model. It is used to transfer GameSession data to and from the frontend.
 * 
 * @author Arhian Albis Ramos
 */
public class GameSessionDTO {
	private String sessionID;
	private boolean isMultiplayer;
	private String codebreaker;
	private String codemaker;
	private int guessNumb;
	private Guess currentGuess;
	private ArrayList<Guess> guesses;
	private GameSettingsDTO gameSettings;
	private int roundNumb;
	private boolean isRoundOver;
	private boolean isGameOver;
	private String winner;
	private String secret;
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public boolean isMultiplayer() {
		return isMultiplayer;
	}

	public void setIsMultiplayer(boolean isMultiplayer) {
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

	public GameSettingsDTO getGameSettings() {
		return gameSettings;
	}

	public void setGameSettings(GameSettingsDTO gameSettings) {
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
	
	public GameSessionDTO(String sessionID, boolean isMultiplayer, String codebreaker, String codemaker, int guessNumb,
			Guess currentGuess, ArrayList<Guess> guesses, GameSettingsDTO gameSettings, int roundNumb,
			boolean isRoundOver, boolean isGameOver, String winner, String secret) {
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

	public GameSessionDTO() {
	}
	
	// Instatiates and returns a new GameSessionDTO object from a GameSession object. 
	public static GameSessionDTO build(GameSession gameSession) {
		return new GameSessionDTO(
				gameSession.getSessionID(),
				gameSession.isMultiplayer(),
				gameSession.getCodebreaker(),
				gameSession.getCodemaker(),
				gameSession.getGuessNumb(),
				gameSession.getCurrentGuess(),
				gameSession.getGuesses(),
				GameSettingsDTO.build(gameSession.getGameSettings()),
				gameSession.getRoundNumb(),
				gameSession.isRoundOver(),
				gameSession.isGameOver(),
				gameSession.getWinner(),
				gameSession.getSecret()
				);
	}

	public String toString() {
		return "GameSessionDTO [sessionID=" + sessionID + ", isMultiplayer=" + isMultiplayer + ", codebreaker="
				+ codebreaker + ", codemaker=" + codemaker + ", guessNumb=" + guessNumb + ", currentGuess="
				+ currentGuess + ", guesses=" + guesses + ", gameSettings=" + gameSettings.toString() + ", roundNumb=" + roundNumb
				+ ", isRoundOver=" + isRoundOver + ", isGameOver=" + isGameOver + ", winner=" + winner + ", secret="
				+ secret + "]";
	}
}
