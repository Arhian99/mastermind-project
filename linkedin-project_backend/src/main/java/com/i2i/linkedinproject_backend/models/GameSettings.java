package com.i2i.linkedinproject_backend.models;

import com.i2i.linkedinproject_backend.DTOs.GameSettingsDTO;

/*
 * Description: This class is used to represent the game settings data associated with a certain game session. The game settings type is used to set the 
 * gamesettings field in the GameSession class @see GameSession
 * 
 * @author Arhian Albis Ramos
 */
public class GameSettings {
	private int numbOfPlayers;				// Number of players in the game session that holds this GameSettings object (either 1 or 2)
	private boolean repeatedDigits;			// Defines whether or not the secret value for the associated game session can have repeated digits. 
	private EDifficulty difficulty;			// Defines the difficulty level for the associated game session. The difficulty level in turn defines the values of secretLength, secretMax, and guessLimit @see EDifficulty
	
	private int secretLength; 	//set by difficulty value: defines the number of digits in the secret value
	private int secretMax; 		//set by difficulty value: defines the high end of the range of each digit in the secret value.
	private int guessLimit; 	//set by difficulty value: defines the total number of guesses available for the codebreaker to crack the code.
	
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
	
	public EDifficulty getDifficulty() {
		return difficulty;
	}
	
	public void setDifficulty(EDifficulty difficulty) {
		this.difficulty = difficulty;
	}
	
	public int getSecretLength() {
		return EDifficulty.getSecretLength(difficulty);
	}
	
	// sets the secretLength in accordance to the difficulty value
	public void setSecretLength(EDifficulty difficulty) {
		this.secretLength = EDifficulty.getSecretLength(difficulty);
	}
	
	public int getSecretMax() {
		return EDifficulty.getSecretMax(difficulty);
	}
	
	// sets the secretMax in accordance to the difficulty value
	public void setSecretMax(EDifficulty difficulty) {
		this.secretMax = EDifficulty.getSecretMax(difficulty);
	}
	
	public int getGuessLimit() {
		return EDifficulty.getGuessLimit(difficulty);
	}
	
	// sets the guessLimit in accordance to the difficulty value
	public void setGuessLimit(EDifficulty difficulty) {
		this.guessLimit = EDifficulty.getGuessLimit(difficulty);
	}

	public GameSettings(int numbOfPlayers, boolean repeatedDigits, EDifficulty difficulty) {
		super();
		this.numbOfPlayers = numbOfPlayers;
		this.repeatedDigits = repeatedDigits;
		this.difficulty = difficulty;
		
		// sets the three fields that depend on the difficulty
		setSecretLength(difficulty);
		setSecretMax(difficulty);
		setGuessLimit(difficulty);
	}
	
	public GameSettings() {
	}

	public static GameSettings buildFromDTO(GameSettingsDTO gameSettingsDTO) {
		return new GameSettings(
				gameSettingsDTO.getNumbOfPlayers(),								// numbOfPlayers
				gameSettingsDTO.isRepeatedDigits(),								// isRepeatedDigits
				EDifficulty.getEnumValue(gameSettingsDTO.getDifficulty())		// difficulty
				);
	}
	
	public String toString() {
		return "GameSettings [numbOfPlayers=" + numbOfPlayers + ", repeatedDigits=" + repeatedDigits + ", difficulty="
				+ difficulty + ", secretLength=" + secretLength + ", secretMax=" + secretMax + ", guessLimit="
				+ guessLimit + "]";
	}	
}
