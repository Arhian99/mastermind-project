package com.i2i.linkedinproject_backend.services;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.linkedinproject_backend.DTOs.FrontendDTO;
import com.i2i.linkedinproject_backend.DTOs.GameSessionDTO;
import com.i2i.linkedinproject_backend.DTOs.GameSettingsDTO;
import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.AlreadyExistsException;
import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.InvalidInputException;
import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.ResourceNotFoundException;
import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.UnableToValidateSessionException;
import com.i2i.linkedinproject_backend.models.GameSession;
import com.i2i.linkedinproject_backend.models.GameSettings;
import com.i2i.linkedinproject_backend.models.Guess;
import com.i2i.linkedinproject_backend.models.Player;
import com.i2i.linkedinproject_backend.repos.GameSessionRepository;

@Service
public class GameService {
//	private static final Logger logger = LoggerFactory.getLogger(GameService.class);

	@Autowired
	GameSessionRepository gameSessionRepo;
	
	@Autowired
	RandomNumberService randomNumberService;
	
	@Autowired
	PlayerService playerService;	
	
	/*
	 * Description: This method recieves data from the frontend in the form of a FrontendDTO, it creates a new singleplayer @see newSingleplayerGame()
	 * or new multiplayer @see newMultiplayerGame() game session. It persists the newly created game session to the database.
	 * 
	 * @param FrontendDTO: holds the data from the frontend necessary to create the game session as well as the player initiating the game session.
	 * @return FrontendDTO which holds the GameSessionDTO that represents the newly created game session
	 */
	public FrontendDTO newGame(FrontendDTO frontendDTO) {
		GameSessionDTO gameSessionDTO = frontendDTO.getCurrentSessionDTO();
		FrontendDTO updatedFrontendDTO;
		
		if(gameSessionDTO.isMultiplayer()) {
			updatedFrontendDTO = newMultiplayerGame(gameSessionDTO);
		}
		else {
			updatedFrontendDTO = newSingleplayerGame(gameSessionDTO);
		}
		
		return updatedFrontendDTO;
	}
	
	/*
	 * Description: This method takes data from the frontend in the form a GameSessionDTO and instantiates a new GameSession object, generates the secret @see RandomNumberService#generateSecret()
	 * in accordance to the specifed GameSettings @see GameSettings and persists the newly created game session to the database. It assumes the Player attempting to create the game session 
	 * already has an account, and their username is held in the codebreaker field of the GameSessionDTO since users are always the codebreakers in singleplayer game sessions.
	 * 
	 * @param GameSessionDTO: data coming from the frontend with the player attempting to create the new game sesison username in the codebreaker field.
	 * @return FrontendDTO: holds a GameSessionDTO which represents the newly created gameSession and the Player object whose username was in the codebreaker field of the passed in GameSessionDTO
	 */
	public FrontendDTO newSingleplayerGame(GameSessionDTO gameSessionDTO) {
		if(gameSessionRepo.existsBySessionID(gameSessionDTO.getSessionID())) {
			throw new AlreadyExistsException("A game session already exists with session ID: "+gameSessionDTO.getSessionID()+", please choose another session ID or resume the session.");
		}

		GameSettingsDTO gameSettingsDTO = gameSessionDTO.getGameSettings();
		GameSettings gameSettings = GameSettings.buildFromDTO(gameSettingsDTO);
		
		// in singleplayer game, the user is ALWAYS the codebreaker and the computer is ALWAYS the codemaker
		Player codebreaker = playerService.findByUsername(gameSessionDTO.getCodebreaker());
		// in singleplayer game, secret is always generated by the computer @see RandomNumberService#generateSecret()
		String secret = randomNumberService.generateSecret(gameSettings.isRepeatedDigits(), gameSettings.getSecretLength(), gameSettings.getSecretMax());
		
		// in singleplayer game, the codebreaker is always the user and the codemaker is always the computer (therefore it is null)
		GameSession gameSession = new GameSession(
				gameSessionDTO.getSessionID(),		// sessionID
				gameSessionDTO.isMultiplayer(),		// isMultiplayer (false)
				gameSessionDTO.getCodebreaker(),	// codebreaker (codebreaker.getUsername())
				null,								// codemaker (null)
				gameSessionDTO.getGuessNumb(),		// guessNumb (0)
				null,								// currentGuess (null)
				new ArrayList<Guess>(),				// guesses ([])
				gameSettings,						// gameSettings
				gameSessionDTO.getRoundNumb(),		// roundNumb (1)
				gameSessionDTO.isRoundOver(),		// isRoundOver (false)
				gameSessionDTO.isGameOver(),		// isGameOver (false)
				null,								// winner (null)
				secret								// secret
				);

		gameSessionRepo.save(gameSession);
		
		return FrontendDTO.build(GameSessionDTO.build(gameSession), codebreaker);
	}
	
	/*
	 * Description: This method takes data from the frontend in the form of a GameSessionDTO and instantiates a new GameSession object. It assumes that the player's role who intiated the game session
	 * is not null. EX: It assumes that if the game session was initiated by the codebreaker the codebreaker field in the GameSessionDTO object is set and the codemaker field is null and vice versa.
	 * It also assumes that if the session was initiated by the codemaker the secret field is not null. Additionally, it assumes that the player attemptint to initiate a new session already has an account.
	 * It persists the newly created session to the database
	 * 
	 * @param GameSessionDTO data coming from the frontend with the player's username attempting to create a new session stored in either the codebreaker or the codemaker field
	 * @return FrontendDTO with the GameSessionDTO represeting the newly created game session and Player object whom initiated the game session.
	 */
	public FrontendDTO newMultiplayerGame(GameSessionDTO gameSessionDTO) {
		if(gameSessionRepo.existsBySessionID(gameSessionDTO.getSessionID())) {
			throw new AlreadyExistsException("A game session already exists with session ID: "+gameSessionDTO.getSessionID()+", please choose another session ID or resume the session.");
		}
		
		GameSettingsDTO gameSettingsDTO = gameSessionDTO.getGameSettings();
		GameSettings gameSettings = GameSettings.buildFromDTO(gameSettingsDTO);
		
		Player playerOne = new Player(); // will store the player object of the user that initiated the game session 

		// this set of if-else statements checks which field is null, whether the codemaker or the codebreaker. 
		// If a field is null then the game must have been initiated by the opposite field. EX: If the codebreaker field is null then the game was initiated by the codemaker.
		if(gameSessionDTO.getCodemaker() != null) {
			// game was initiated by the codemaker --> get codemaker AND validate secret. This method assumes that if the game session was initiated by the codemaker, the secret has already been set in the frontend.
			playerOne = playerService.findByUsername(gameSessionDTO.getCodemaker());
			validateSecret(gameSessionDTO.getSecret(), gameSettings);

		} else if(gameSessionDTO.getCodebreaker() != null) {
			// game was initiated by the codebreaker --> get codebreaker BUT dont validate secret. This method assumes that since the game was intiated by the codebreaker, the secret field is null since it has not been set.
			playerOne = playerService.findByUsername(gameSessionDTO.getCodebreaker());
		}

		GameSession gameSession = new GameSession(
				gameSessionDTO.getSessionID(),		// sessionID
				gameSessionDTO.isMultiplayer(),		// isMultiplayer (true)
				gameSessionDTO.getCodebreaker(),	// codebreaker ( playerOne.getUsername() | null )
				gameSessionDTO.getCodemaker(),		// codemaker ( playerOne.getUsername() | null )
				gameSessionDTO.getGuessNumb(),		// guessNumb (0)
				gameSessionDTO.getCurrentGuess(),	// currentGuess (null)
				new ArrayList<Guess>(),				// guesses ([])
				gameSettings,						// gameSettings
				gameSessionDTO.getRoundNumb(),		// roundNumb (1)
				gameSessionDTO.isRoundOver(),		// isRoundOver (false)
				gameSessionDTO.isGameOver(),		// isGameOver (false)
				gameSessionDTO.getWinner(),			// winner (null)
				gameSessionDTO.getSecret()			// secret (null)
				);
				
		gameSessionRepo.save(gameSession);
		return FrontendDTO.build(GameSessionDTO.build(gameSession), playerOne);
	}
	
	/*
	 * Description: This method implements a decision tree to retrieve a game session and/or add a player to a game session based on the sessionID, player role, and player username.
	 * The method ensures that the player username requesting the session should have access to the session. In the case of singleplayer gameSessions the method only returns 
	 * the requested game session if the username passed in is the codebreaker for the specified session (since the codemaker is null for singleplayer sessions @see newSingleplayerGame()).
	 * In the case of multiplayer sessions, if the session is not fully started (that is the session has a null codebreaker or a null codemaker field) AND the username passed into this method 
	 * (the username requesting this session) is NOT equal to the non-vacant role; the the username will be set as the vacant role, the newly updated session will be persisted to 
	 * the database and the sesison will be returned. EX: if codebreaker=test1 and codemaker=null, and the username passed into this method is NOT test1 then the username passed into this method
	 * will be set as the codemaker for that session. If the username passed into this method is test1, the session will be returned and the codemaker will remain null. In the case the multiplayer
	 * session is fully started, that is both codebreaker and codemaker are NOT null, the this method will only return the game session if the username passed into this method (the person requesting
	 * the session) is either the codebreaker or the codemaker.
	 * 
	 * @parma sessionID: session ID of the session being requested
	 * @param username: person requesting the session
	 * @throws UnableToValidateSessionException
	 * @return GameSessionDTO: session being requested which matches the passed in sessionID
	 */
	public GameSessionDTO getAndValidateSession(String sessionID, String username) {
		GameSession gameSession = gameSessionRepo.findBySessionID(sessionID).orElseThrow(() -> new ResourceNotFoundException("No game session found with session ID: "+sessionID));
		
		String codebreakerUsername = gameSession.getCodebreaker();
		String codemakerUsername = gameSession.getCodemaker();
	
		if(gameSession.isMultiplayer()) {
			// validates multiplayer sessions... @see description
			// if there is a vacant role in the session..
			if(codebreakerUsername==null || codemakerUsername==null) {
				// if the vacant role is the codebreaker
				if(codebreakerUsername==null) {
					// BUT if the person requesting the session is the codemaker 
					if(username.equals(codemakerUsername)) {
						// simply return the session to the codemaker, leaving the other role vacant
						return GameSessionDTO.build(gameSession);
					} else {
						// otherwise if the person requesting the session is NOT the codemaker
						gameSession.setCodebreaker(username);	// set this person as the codebreaker (fill the vacant role), persist and return
						gameSessionRepo.save(gameSession);
						return GameSessionDTO.build(gameSession);
					}
					// if the vacant role is the codemaker
				} else if(codemakerUsername==null) {
					// BUT the person requesting the sesison is the codebreaker
					if(username.equals(codebreakerUsername)) {
						// simply return the session to the codebreaker
						return GameSessionDTO.build(gameSession);
					} else {
						// otherwise if the person requesting the session is NOT the codebreaker
						gameSession.setCodemaker(username);		// set this person as the codemaker (fill the vacant role), persist and return
						gameSessionRepo.save(gameSession);
						return GameSessionDTO.build(gameSession);
					}
				}
			} else {
				// if there are not vacant role and the person requesting the sesison is either the codebreaker or the codemaker, return them the sesison
				if(username.equals(codebreakerUsername) || username.equals(codemakerUsername)) {
					return GameSessionDTO.build(gameSession);
				}else throw new UnableToValidateSessionException("Username "+username+" does not match session ID "+sessionID);
			}
		} else {
			// validates singleplayer sessions @see description
			if(username.equals(codebreakerUsername)) {
				return GameSessionDTO.build(gameSession);
			} else throw new UnableToValidateSessionException("Username "+username+" does not match session ID "+sessionID);
		}

		return GameSessionDTO.build(gameSession);
	}
	
	/*
	 * Description: This method takes a data from the frontend in the form a FrontendDTO which contains the Player object attempting to submit a guess and the GameSessionDTO with 
	 * the data associated with the game session, including the guess being submitted in the currentGuess field of the GameSessionDTO. This method checks if the player attempting 
	 * to submit the guess is the codebreaker associated with the session, validates the guess @see validateGuess(), if the game session is over and the winner has been set it
	 * returns the game session, otherwise it retrieves the GameSession model from the database, it sets the current guess field in the GameSession models and then updates gameSession
	 * model with the value returned from the checkWinner methods @see checkSingleplayerWinner() and @see checkMultiplayerWinner()
	 * 
	 * @param FrontendDTO data coming from the frontend containing the Player attempting to submit the guess and the GameSessionDTO object with the guess being submitted in the currentGuess field.
	 * @return updated GameSessionDTO
	 */
	public GameSessionDTO postGuess(FrontendDTO frontendDTO) {
		GameSessionDTO gameSessionDTO = frontendDTO.getCurrentSessionDTO();
		
		GameSession gameSession = gameSessionRepo.findBySessionID(gameSessionDTO.getSessionID())
												 .orElseThrow(() -> new ResourceNotFoundException("No session found with SessionID: "+gameSessionDTO.getSessionID()));
		
		// checks if the player attempting to submit guess is the codebreaker of the associated session
		if(!frontendDTO.getCurrentPlayer().getUsername().equals(gameSession.getCodebreaker())) {			
			throw new InvalidInputException("Only codebreaker is able to submit guess!");
		}
		
		// if the game sesison is over, it returns the same game session
		if(gameSession.isGameOver() && !(gameSession.getWinner() == null)) {
			return GameSessionDTO.build(gameSession);
		}
		
		// otherwise validates the guess and sets the current guess field in the gameSession object 
		validateGuess(gameSessionDTO.getCurrentGuess(), gameSession.getGameSettings());
		gameSession.setCurrentGuess(gameSessionDTO.getCurrentGuess());
		
		// updates the gameSession by calling checkMultiplayerWinner() OR checkSingleplayerWinner() methods
		if(gameSession.isMultiplayer()) gameSession = checkMultiplayerWinner(gameSession);
		else gameSession = checkSingleplayerWinner(gameSession);
		
		// persists the updated gameSession
		gameSessionRepo.save(gameSession);
		return GameSessionDTO.build(gameSession);
	}

	/*
	 * Description: This method implements a decision tree algorithm to determine the winner of a singleplayer game session and or call the checkGuess() method to count a guess and update
	 * the game session. First it checks if there is space for at least one more guess (guessNumb<guessLimit). If there is space for at least one more guess, then it checks if the current guess
	 * has already been counted. The currentGuess in a session only gets counted after callung the checkGuess() method @see checkGuess() which generates and sets the feedback field @see generateFeedback()
	 *  in the guess object and increments the guessNumb in the game session, therefore Guess objects with a null feedback field have not been counted. If there is space for at least one more guess and the 
	 *  current guess has NOT already been counted, it calls the checkGuess() method which returns an updated gameSession and then recurses with the updated gameSession and the already counted guess.
	 *  On the other hand, If there is space for at least one more guess and the currentGuess HAS already been counted, it checks if this guess is a full match if so, it sets the game as over if not, allows the game 
	 *  to continue. On the other branch of the tree... If there is NOT space for one more guess (guessNumb==guessLimit) and the currentGuess has not already been counted then the codebreaker has run 
	 *  out of guessses so the game is set to be over. BUT if the currentGuess has been counted then we check if its a full match, if so the codebreaker wins otherwise the codemaker wins (since there is not space for another guess.
	 *  
	 *  @param GameSession this method takes a gameSession and assumes the currentGuess field is set 
	 *  @return GameSession an updated gameSesion with the currentGuess field set with a Guess object which contains an index and feedback fields set as well.
	 */
	public static GameSession checkSingleplayerWinner(GameSession gameSession) {
		Guess currentGuess = gameSession.getCurrentGuess();
		GameSettings gameSettings = gameSession.getGameSettings();
		
		// checks if there is space for one more guess in the current game session @see GameSession @see GameSettings
		if(gameSession.getGuessNumb() < gameSettings.getGuessLimit()) {
			// there is space for at least one more guess (since guessNumb<guessLimit)
			
			// if the currentGuess has not been counted... 
			if(currentGuess.getFeedback() == null) {
				/*
				 * a guess only gets counted after calling the checkGuess() method) @see checkGuess() which generates 
				 * feedback for that guess @see generateFeedback() and increments the guessNumb, if a guess object 
				 * has a null feedback field, this indicates it has not been counted @see checkGuess
				 */
				
				gameSession = checkGuess(gameSession); // count the guess and generate feedback by calling checkGuess() method 
				checkSingleplayerWinner(gameSession);	// recurse with the new gameSession
				
			} else {
				// if the guess has been counted, check if its a full match...
				if(isFullMatch(currentGuess, gameSession.getSecret())) {
					// if its a full match the codebreaker wins
					gameSession.setWinner(gameSession.getCodebreaker());
					gameSession.setRoundOver(true);
					gameSession.setGameOver(true);;
					
				} else if(!isFullMatch(currentGuess, gameSession.getSecret())) {
					// if its not a full match the game continues...
					gameSession.setWinner(null);
					gameSession.setRoundOver(false);
					gameSession.setGameOver(false);
				}
			}
			
		} else if(gameSession.getGuessNumb() == gameSettings.getGuessLimit()) {
			// there is NOT space for one more guess (guessNumb = guessLimit)
			
			/*
			 *  if the currentGuess has not been counted (a guess only gets counted after calling the checkGuess() method) @see checkGuess() which generates 
			 *  feedback for that guess @see generateFeedback() and increments the guessNumb, if a guess object has a null feedback field, this indicates it has not been counted @see checkGuess
			 */
			if(currentGuess.getFeedback() == null) {
				// since the guessNumb==guessLimit and the current guess has not been counted, the codebreaker has run out of guesses
				gameSession.setWinner("Computer");
				gameSession.setRoundOver(true);
				gameSession.setGameOver(true);
				
			} else {
				// the feedback field for this guess is not null therefore it has been counted, if its a full match the codebreaker wins
				if(isFullMatch(currentGuess, gameSession.getSecret())) {
					gameSession.setWinner(gameSession.getCodebreaker());
					gameSession.setRoundOver(true);
					gameSession.setGameOver(true);
					
				} else if(!isFullMatch(currentGuess, gameSession.getSecret())) {
					// currentGuess is not a full match and the guessNumb==guessLimit (there is no more space for one more guess) therefore the codebreaker has run out guesses
					gameSession.setWinner("Computer");
					gameSession.setRoundOver(true);
					gameSession.setGameOver(true);
				}
			}
		}
		
		return gameSession;
	}
	
	
	/*
	 * Description: Currently this method contains the exact same logic as checkSingleplayerWinner() but it is left here for future implementation of different winning logic in multiplayer sessions. For example, implement rounds where 
	 * player take turns being the codebreaker. 
	 */
	public static GameSession checkMultiplayerWinner(GameSession gameSession) {
		Guess currentGuess = gameSession.getCurrentGuess();
		GameSettings gameSettings = gameSession.getGameSettings();
		
		if(gameSession.getGuessNumb() < gameSettings.getGuessLimit()) {
			if(currentGuess.getFeedback() == null) {
				gameSession = checkGuess(gameSession);
				checkMultiplayerWinner(gameSession);
				
			} else {
				if(isFullMatch(currentGuess, gameSession.getSecret())) {
					gameSession.setWinner(gameSession.getCodebreaker());
					gameSession.setRoundOver(true);
					gameSession.setGameOver(true);;
					
				} else if(!isFullMatch(currentGuess, gameSession.getSecret())) {
					gameSession.setWinner(null);
					gameSession.setRoundOver(false);
					gameSession.setGameOver(false);
				}
			}
			
		} else if(gameSession.getGuessNumb() == gameSettings.getGuessLimit()) {
			if(currentGuess.getFeedback() == null) {
				gameSession.setWinner(gameSession.getCodemaker());
				gameSession.setRoundOver(true);
				gameSession.setGameOver(true);
				
			} else {
				if(isFullMatch(currentGuess, gameSession.getSecret())) {
					gameSession.setWinner(gameSession.getCodebreaker());
					gameSession.setRoundOver(true);
					gameSession.setGameOver(true);
					
				} else if(!isFullMatch(currentGuess, gameSession.getSecret())) {
					gameSession.setWinner(gameSession.getCodemaker());
					gameSession.setRoundOver(true);
					gameSession.setGameOver(true);
				}
			}
		}
		return gameSession;
	}

	
	/*
	 * Description: This method takes a gameSession with the currentGuess field holding a Guess object that has NOT been counted. This method assumes the Guess object in the 
	 * currentGuess field of the GameSession has not been counted and its feedback field is null. This method then generates feedback for the current guess @see generateFeedback()
	 * then updates the currentGuess field in the GameSession with the updated Guess object, it increments the guesNumb in the GameSession (and therefore counts the currentGuess),
	 * it sets the currentGuess Guess object index, and adds the currentGuess to the guesses array within the gameSession. It returns the updated gameSession.
	 * 
	 * @param GameSession gameSession holding a Guess in the currentGuess field which has not been counted or added to the guesses list
	 * @return GameSession and updated gameSesison after the currentGuess now has feedback @see gneerateFeedback() and index generated, the guessNumb has been incremented and the guess was added to the guesses list.
	 */
	public static GameSession checkGuess(GameSession gameSession) {
		Guess currentGuess = gameSession.getCurrentGuess();
		
		currentGuess = generateFeedback(currentGuess, gameSession.getSecret());
		gameSession.setCurrentGuess(currentGuess);
		gameSession.setGuessNumb(gameSession.getGuessNumb()+1);
		gameSession.getCurrentGuess().setIndex(gameSession.getGuessNumb());
		gameSession.getGuesses().add(currentGuess);
		
		return gameSession;
	}
	
	
	/*
	 * Description: This method takes a FrontendDTO @see FrontendDTO which holds the Player attempting to set the secret as well as the GameSessionDTO which holds the secret that 
	 * the player wants to set. This method checks whether the player attempting to set the secret is the codemaker, if so it validates the secret @see validateSecret(), sets the secret 
	 * on the GameSession model @see GameSession and persists the newly updated GameSession model to the database @see GameSessionRepository.
	 * 
	 * @param FrontendDTO: holds data coming from the frontend, the currentPlayer field in frontendDTO must be the codemaker in the GameSessionDTO in order to set the secret
	 * @return FrontendDTO: updated frontendDTO after secret has been set and the updated GameSession (with the new secret) has been persisted to the database.
	 */
	public FrontendDTO setSecret(FrontendDTO frontendDTO) {
		GameSessionDTO gameSessionDTO = frontendDTO.getCurrentSessionDTO();
		Player currentPlayer = frontendDTO.getCurrentPlayer();
		String secret = gameSessionDTO.getSecret();
		
		GameSession gameSession = gameSessionRepo
				.findBySessionID(gameSessionDTO.getSessionID())
				.orElseThrow(() -> new ResourceNotFoundException("No session found with sessionID: "+gameSessionDTO.getSessionID()));
	
		// checks if the player attempting to set the secret is the codemaker of the GameSession
		if(currentPlayer.getUsername().equals(gameSession.getCodemaker())) {
			validateSecret(secret, gameSession.getGameSettings());		// validates the secret before setting it.
			gameSession.setSecret(secret);
			gameSessionRepo.save(gameSession);
			
		} else throw new InvalidInputException("Only codemaker is allowed to set the secret.");
	
		GameSessionDTO updatedSession = GameSessionDTO.build(gameSession);

		return FrontendDTO.build(updatedSession, currentPlayer);
	}
	
	/*
	 * Description: This method validates a guess for length and ensures it only contains digits.
	 * 
	 * @param Guess guess object to be validated
	 * @param GameSettings gameSettings object to validate against.
	 * @throws InvalidInputException if guess is not valid.
	 */
	public static void validateGuess(Guess guess, GameSettings gameSettings) {
		String guessStr = guess.getGuess();
		String errorMessage="";
		
		if(guessStr.length() != gameSettings.getSecretLength()) {
			errorMessage = errorMessage+"Guess must be "+gameSettings.getSecretLength()+" digits in length. ";
		} 
		
		if(!isOnlyDigits(guessStr)) {
			errorMessage = errorMessage+"Guess must only contain numerical digits.";
		}
		
		if(errorMessage.equals("")) return;
		else throw new InvalidInputException(errorMessage);
	}
	
	/*
	 * Description: This method recieves a string and checks whether this string contains only integers.
	 * 
	 * @param numbString: string which you want to check
	 * @return true if string passed in contains only digits, false otherwise
	 */
	public static boolean isOnlyDigits(String numbString) {
		for(int i=0; i<numbString.length(); i++) {
			char currentChar = numbString.charAt(i);
			if(!Character.isDigit(currentChar)) return false;
		}
		return true;
	}
	
	/*
	 * Description: this method validates a secret passed in, in accordance to the passed in GameSettings @see GameSettings.
	 * Validates secret for length, whether or not it contains only numbers, for the range of each of its digits, and whether it has repeats if 
	 * repeats are not allowed.
	 * 
	 * @param: secret: the secret that requires validation
	 * @param: GameSettings: used to validate the secret against.
	 * @throws InvalidInputException if the secret is not valid.
	 */
	public static void validateSecret(String secret, GameSettings gameSettings) {
		String errorMessage="";
		
		// validates for length
		if(secret.length() != gameSettings.getSecretLength()) {
			errorMessage="Secret must be "+gameSettings.getSecretLength()+" digits in length. ";
		}
		
		// ensures the secret does not contain letters.
		if(!isOnlyDigits(secret)) {
			errorMessage=errorMessage+"Secret must only contain numerical digits. ";
			throw new InvalidInputException(errorMessage);
		}
		
		// ensures each of the digits in secret has appropriate range
		for(int i=0; i<secret.length(); i++) {
			int currentDigit = Character.getNumericValue(secret.charAt(i));
			
			if(currentDigit > gameSettings.getSecretMax() || currentDigit < 0) {
				errorMessage=errorMessage+"Secret digits must range from 0-"+gameSettings.getSecretMax();
				break;
			}
		}
		
		// ensures that secret does not have repeats if repeated digits are not allowed
		if(RandomNumberService.hasRepeats(secret) && !gameSettings.isRepeatedDigits()) {
			errorMessage=errorMessage+"Secret must not contain repeated digits. ";
		}
		
		if(errorMessage.equals("")) return;
		else throw new InvalidInputException(errorMessage);
	}
	
	
	/*
	 *  Description: This method takes a Guess object and a secret value, it compares the guess field in the Guess object with the secret value and 
	 *  sets the feedback field in the Guess object @see Guess. It sets the feedback field with the number of values in guess that are also present in 
	 *  the secret (correctNumbers) and with the number of these correctNumbers that are also in the correct position (correctLocations). All correctLocations
	 *  are also correctNumbers BUT not all correctNumbers are correctLocations. This method assumes guess and secret are equal length. 
	 *  
	 *  @param Guess - The user-submitted guess value, the value that is to be checked against the secret value. This Guess object has a null feedback field
	 *  @param secret - The randomly generated "secret" value which the user is trying to guess, the value for which guess is checked against. 
	 *  @return Guess - Returns an updated Guess object with a non-null feedback field.
	 *  
	 */
 	public static Guess generateFeedback(Guess guess, String secret) {
		int correctLocations=0, correctNumbers=0;
		// correctLocations -> the number of values in guess that match BOTH in value and location in secret.
		// correctNumbers -> the number of values in guess that are also present in secret but not necessarily (although they may be) in their correct locations.
		String guessStr = guess.getGuess();
		
		// By definition HashSets hold unique values.
		HashSet<Character> guessFreq = new HashSet<>(); 	// holds the values that have shown up in guess BUT have NOT yet shown up in secret.
		HashSet<Character> secretFreq = new HashSet<>();	// holds the values that have shown up in secret BUT have NOT yet shown up in guess.
		
		// iterates through each of the digits in both secret and guess.
		for(int i=0; i<secret.length(); i++) {
			// if the current digit match each other, then that digit is in a correct location, we increment the correctLocation counter and move on.
			if(guessStr.charAt(i)==secret.charAt(i)) {
				correctLocations++;
			} else {
				// if the current digits dont match each other...
				
				// check if the current digit in the secret has shown up in the guess
				if(guessFreq.contains(secret.charAt(i))) {
					// if so then that digit is a correct Number (since it is in the secret and the guess)
					correctNumbers++;
					// since we alredy counted that digit as a correct number we remove it from guessFreq array since we dont want to double count
					guessFreq.remove(secret.charAt(i));
				} else {
					// if the current digit in secret has NOT shown up in the guess, then it has not been counted as a correctNumber so we add it to secretFreq set
					// for future reference in case that digit shows in the guess in the future.
					secretFreq.add(secret.charAt(i));
				}
				
				// likewise, check if the current digit in the guess has shown up before in secret and repeat same process as above but for the secret.
				if(secretFreq.contains(guessStr.charAt(i))) {
					correctNumbers++;
					secretFreq.remove(guessStr.charAt(i));
				} else {
					guessFreq.add(guessStr.charAt(i));
				}
			}
		}
		
		// each correctLocation was not counted as a correctNumber in the iteration through the guess and secret but each correct location by definition is also a correct number.
		correctNumbers+=correctLocations;
		guess.setFeedback(correctLocations+" correctLocation(s)<br />"+correctNumbers+" correctNumber(s)");
		return guess;
	}
	
 	
 	/*
 	 * Description: checks whether the guess and the secret match each other exactly. @returns true if they do, false otherwise
 	 * 
 	 * @param Guess: guess which you want to compare to secret
 	 * @param secret: secret to which guess is compared to.
 	 * @return true if they are equal. false otherwise
 	 */
	public static boolean isFullMatch(Guess guess, String secret) {
		if(guess.getGuess().equals(secret)) return true;
		else return false;
	}
	
}