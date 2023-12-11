package com.i2i.linkedinproject_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.linkedinproject_backend.DTOs.FrontendDTO;
import com.i2i.linkedinproject_backend.DTOs.GameSessionDTO;
import com.i2i.linkedinproject_backend.services.AuthenticationService;
import com.i2i.linkedinproject_backend.services.GameService;
import com.i2i.linkedinproject_backend.services.PlayerService;
import com.i2i.linkedinproject_backend.services.RandomNumberService;


/*
 * Description: This is a controller class. All request to /game/** endpoints get mapped to this controller class and its methods. This class exposes endpoints for 
 * creating a new game session, posting a guess to an already created game session, retrieving a game session or setting a secret code to an already created game session.
 * 
 * @author Arhian Albis Ramos
 */
@RestController
@RequestMapping("/game")
@CrossOrigin(origins="*")
public class GameController {
	@Autowired
	GameService gameService;
	
	@Autowired 
	RandomNumberService randomNumberService;
	
	@Autowired
	PlayerService playerService;
	
	@Autowired
	AuthenticationService authService;
	
//	private static final Logger logger = LoggerFactory.getLogger(GameController.class);

	/*
	 * Description: This handles POST requests to /game/new endpoint. It recieves a FrontendDTO @see FrontendDTO, creates a new GameSession @see GameService#newGame()
	 * @see GameService.newSingleplayerGame() and  @see GameService.newMultiplayerGame() and returns the newly created GameSession in the form of a 
	 * GameSessionDTO inside FrontendDTO with HttpStatus 201. @see GameSessionDTO and FrontendDTO 
	 * 
	 * @param FrontendDTO: object recieved from the frontend with the GameSessionDTO and Player objects that hold the necessary data for creating new game session.
	 * @return FrontendDTO: holds newly created GameSessionDTO.
	 */
	@PostMapping("/new")
	public ResponseEntity<?> newSession(@RequestBody FrontendDTO frontendDTO){
		FrontendDTO newSession = gameService.newGame(frontendDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newSession);
	}
	
	/*
	 * Description: This method handles POST requests to /game/postGuess endpoint. It takes a FrontendDTO which holds the player attempting to post the guess as well as 
	 * a GameSessionDTO with the guess in the currentGuess field @see GameService#postGuess(). It returns an updated GameSessionDTO. @see GameService#postGuess().
	 * 
	 * @param FrontendDTO: holds Player attempting to post the guess and GameSessionDTO with sessionID field and currentGuess field @see GameService#postGuess()
	 * @return GameSessionDTO: updated game session.
	 */
	@PostMapping("/postGuess")
	public ResponseEntity<?> postGuess(@RequestBody FrontendDTO frontendDTO){
		GameSessionDTO updatedSession = gameService.postGuess(frontendDTO);
		
		return ResponseEntity.status(HttpStatus.OK).body(updatedSession);
	}
	
	/*
	 * Description: This method handles POST requests to /game/setSecret endpoint. It takes a FrontendDTO with Player attempting to set the secret and GameSessionDTO to which
	 * the secret is being set to @see GameService#setSecret(). It returns the updated GameSessionDTO inside a FrontendDTO.
	 * 
	 * @param FrontendDTO: holds Player attempting to set the secret (must be the codemaker) and GameSessionDTO with sessionID and secret field null @see GameService#setSecret()
	 * @return FrontendDTO: holds an updated GameSessionDTO with the secret field not null. @see GameService#setSecret().
	 */
	@PostMapping("/setSecret") 
	public ResponseEntity<?> setSecret(@RequestBody FrontendDTO frontendDTO) {
		FrontendDTO updatedSession = gameService.setSecret(frontendDTO);
		
		return ResponseEntity.status(HttpStatus.OK).body(updatedSession);
	}

	/*
	 * Description: This method handles GET requests to /game/session?sessionID=<sessionID>&username=<username> endpoint. It returns the GameSessionDTO associated with the session ID @see GameService#getSession()
	 * 
	 * @param sessionID: session ID associated with the requested GameSessionDTO
	 * @param username: username requesting the session
	 * @return GameSessionDTO: the gameSessionDTO associated with the sessionID. @see GameService#getSession().
	 */
	@GetMapping("/session")
	public ResponseEntity<?> getSession(@RequestParam("sessionID") String sessionID, @RequestParam("username") String username){
		GameSessionDTO gameSession = gameService.getAndValidateSession(sessionID, username);
		
		return ResponseEntity.status(HttpStatus.OK).body(gameSession);
	}
	
}
