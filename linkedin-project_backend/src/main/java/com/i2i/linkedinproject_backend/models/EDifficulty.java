package com.i2i.linkedinproject_backend.models;

/*
 * Description: This enum type represents the 3 difficulty levels available in the game. Each difficulty level is mapped to its string representation. In addition
 * each difficulty level is also mapped to a guess limit which represents the number of guesses available for each difficulty level, to a secret max which represents
 * the higher end of the range of each of the secret code digits, as well as the secret length which represents the number of digits in the secret code.
 * @see GameSettings @see RandomNumberService#getURI()
 * 
 * @author Arhian Albis Ramos
 */
public enum EDifficulty {
	DIFFICULTY_EASY("easy"),
	DIFFICULTY_MEDIUM("medium"),
	DIFFICULTY_HARD("hard");
	
	String key;
	
	EDifficulty(String key) {
		this.key = key;
	}
	
	/*
	 * Description: This method takes a difficulty level enum and returns the secretMax in integer form. The secret max represents the higher end of the range of each
	 * of the secret code digits. ie: DIFFICULTY_EASY returns 7 so each secret code digit for difficulty "easy" ranges from 0 to 7. @see GameSettings @RandomNumberService#getURI()
	 * 
	 * @param EDifficulty: the desired difficulty for which you want to know the secret max of.
	 * @return secretMax: The higher end of the range of each of the secret max digits. 
	 */
	public static int getSecretMax(EDifficulty difficulty) {
		switch(difficulty) {
		case DIFFICULTY_EASY: return 7;
		case DIFFICULTY_MEDIUM: return 8;
		case DIFFICULTY_HARD: return 9;
		default: throw new IllegalArgumentException();
		}
	}
	
	/*
	 * Description: This method takes a difficulty level enum and returns the guess limit in integer form. The guess limit represents the total number of guess the codebreaker
	 * has to break the code. ie: DIFFICULTY_EASY returns 10 therfore for difficulty "easy" the codebreaker has 10 guesses to break the code. 
	 * @see GameSettings @see GameService#checkSingleplayerWinner() @see GameService#checkMultiplayerWinner() 
	 * 
	 * @param EDifficulty: the desired difficulty for which you want to know the guess limit of.
	 * @return guessLimit: the total number of guesses the codebreaker has to break the code.
	 */
	public static int getGuessLimit(EDifficulty difficulty) {
		switch(difficulty) {
		case DIFFICULTY_EASY: return 10;
		case DIFFICULTY_MEDIUM: return 9;
		case DIFFICULTY_HARD: return 8;
		default: throw new IllegalArgumentException();
		}
	}
	
	/*
	 * Description: This method takes a difficulty level enum and returns the secret length in integer form. The secret length represents the number of digits present in the secret code
	 * ie: DIFFICULTY_EASY returns 4 therefore for difficulty "easy" the secret code is 4 digits long. 
	 * @see GameSettings 
	 * 
	 * @param EDifficulty: the desired difficulty for which you want to know the secret length of.
	 * @return secretLength: the number of digits in the secret code for the specified difficulty.
	 */
	public static int getSecretLength(EDifficulty difficulty) {
		switch(difficulty) {
		case DIFFICULTY_EASY: return 4;
		case DIFFICULTY_MEDIUM: return 5;
		case DIFFICULTY_HARD: return 6;
		default: throw new IllegalArgumentException();
		}
	}
	
	/*
	 * Description: This method takes an EDifficulty type  and returns the string value associated with it.
	 * 
	 * @param EDifficulty: the desired difficulty for which you want to know the string value for.
	 * @return difficulty: the string value for the specifed EDifficulty type
	 */
	public static String getStringValue(EDifficulty difficulty) {
		switch(difficulty) {
		case DIFFICULTY_EASY: return "easy";
		case DIFFICULTY_MEDIUM: return "medium";
		case DIFFICULTY_HARD: return "hard";
		default: throw new IllegalArgumentException();
		}
	}
	
	/*
	 * Description: This method takes an takes a string value and returns the EDifficulty type mapped to specified string value.
	 * 
	 * @param difficulty: The string value for which you want to know the EDifficulty type.
	 * @return EDifficulty: The desired EDifficulty mapped to the specified string value
	 */
	public static EDifficulty getEnumValue(String keyVal) {
		switch(keyVal){
		case "easy": return DIFFICULTY_EASY;
		case "medium": return DIFFICULTY_MEDIUM;
		case "hard": return DIFFICULTY_HARD;
		default: throw new IllegalArgumentException();
		}
	}
}
