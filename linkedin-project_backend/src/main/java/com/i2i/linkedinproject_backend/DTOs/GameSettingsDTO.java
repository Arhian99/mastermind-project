package com.i2i.linkedinproject_backend.DTOs;

import com.i2i.linkedinproject_backend.models.EDifficulty;
import com.i2i.linkedinproject_backend.models.GameSettings;

/*
 * Description: This class serves as a representation of the GameSettings Model. It is used to transfer GameSettings data to and from the frontend.
 * 
 * @author Arhian Albis Ramos
 */
public class GameSettingsDTO {
	private int numbOfPlayers;
	private boolean repeatedDigits;
	private String difficulty;

	private int secretLength; 	//set by difficulty
	private int secretMax; 		//set by difficulty
	private int guessLimit; 	//set by difficulty
	
	public int getNumbOfPlayers() {
		return numbOfPlayers;
	}
	
	public void setNumbOfPlayers(int numbOfPlayers) {
		this.numbOfPlayers = numbOfPlayers;
	}
	
	public boolean isRepeatedDigits() {
		return repeatedDigits;
	}
	
	public void setRepeatedDigits(boolean repeatedDigits) {
		this.repeatedDigits = repeatedDigits;
	}
	
	public String getDifficulty() {
		return difficulty;
	}
	
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	
	public int getSecretLength() {
		return secretLength;
	}
	
	public void setSecretLength(int secretLength) {
		this.secretLength = secretLength;
	}
	
	public int getSecretMax() {
		return secretMax;
	}
	
	public void setSecretMax(int secretMax) {
		this.secretMax = secretMax;
	}
	
	public int getGuessLimit() {
		return guessLimit;
	}
	
	public void setGuessLimit(int guessLimit) {
		this.guessLimit = guessLimit;
	}

	public GameSettingsDTO(int numbOfPlayers, boolean repeatedDigits, String difficulty) {
		this.numbOfPlayers = numbOfPlayers;
		this.repeatedDigits = repeatedDigits;
		this.difficulty = difficulty;
		
		setSecretLength(EDifficulty.getSecretLength(EDifficulty.getEnumValue(difficulty)));
		setSecretMax(EDifficulty.getSecretMax(EDifficulty.getEnumValue(difficulty)));
		setGuessLimit(EDifficulty.getGuessLimit(EDifficulty.getEnumValue(difficulty)));

	}
	
	public GameSettingsDTO() {
	}
	
	public static GameSettingsDTO build(GameSettings gameSettings) {
		return new GameSettingsDTO(
				gameSettings.getNumbOfPlayers(),
				gameSettings.isRepeatedDigits(),
				EDifficulty.getStringValue(gameSettings.getDifficulty())
				);
	}

	public String toString() {
		return "GameSettingsDTO [numbOfPlayers=" + numbOfPlayers + ", repeatedDigits=" + repeatedDigits
				+ ", difficulty=" + difficulty + ", secretLength=" + secretLength + ", secretMax=" + secretMax
				+ ", guessLimit=" + guessLimit + "]";
	}
	
	
}
