import React, {useEffect} from 'react'
import { Container, Button } from 'react-bootstrap'
import useGame from '../Hooks/useGame';
import { useNavigate } from 'react-router-dom';

function Instructions({setActivePage}) {
  const navigate = useNavigate();
  const {setGameSession, setCurrentPlayer} = useGame();

  useEffect(() => {
    setGameSession(null);
    setCurrentPlayer(null);
  }, [])

  return (
    <Container className='mt-5 border border-4 rounded h-80 w-80' >
        <h1 className='mb-4 '>Instructions</h1>
        <h4> Player Settings: </h4>
        <Container className='p-0 m-0 fs-6'>
          <ul>
            <li>
              <span className='fw-bold'>New Players: </span>Choose any username and password and the system will make an account for you. Please remember these credentials for future use.
            </li>
            <li>
              <span className='fw-bold'>Returning players: </span>Enter your username and password.
            </li>
          </ul>
        </Container>

        <h4> Game Settings: </h4>
        <Container className='p-0 m-0 fs-6'>
          <ul>
            <li>
              <span className='fw-bold'>Room Name/SessionID: </span>This is a unique identifier for your game session. Remember this information as it is used to come back to a game session you have already started or to join a game session in multiplayer mode. 
            </li>
            <li>
              <span className='fw-bold'>Singleplayer Mode: </span>You are the codebreaker and the computer is the codemaker. You will have to guess the computer generated code. The computer code follows the game settings and difficulty level. 
            </li>
            <li>
              <span className='fw-bold'>Multiplayer Mode: </span>The player that starts the game session can pick whether they would like to play as the codemaker or the codebreaker, in addition to the other game settings. The player that joins the session gets assigned the other role. The secret code set by the codemaker gets validated against the game settings selected at the start of the game.
            </li>
            <li>
              <span className='fw-bold'>Repeated Digits: </span>When turned on, it allows the secret code to have repeated digits (does not mean it will), when turned off all the digits in the secret code will be unique.
            </li>
          </ul>
        </Container>

        <h4> Difficulty: </h4>
        <Container className='p-0 m-0 fs-6'>
          <ul>
            <li>
              <span className='fw-semibold'>Easy: </span>Codebreaker has 10 guesses to break a 4 digit code, each digit ranges from 0 to 7.
            </li>
            <li>
              <span className='fw-semibold'>Medium: </span>Codebreaker has 9 guesses to break a 5 digit code, each digit ranges from 0 to 8.
            </li>
            <li>
              <span className='fw-semibold'>Hard: </span>Codebreaker has 8 guesses to break a 6 digit code, each digit ranges from 0 to 9.
            </li>
          </ul>
        </Container>

        <Button className='mt-4 me-2 fw-semibold bg-black border-black' onClick={() => navigate("/")}>Back</Button>
        <Button className='mt-4 fw-semibold bg-black border-black' onClick={() => setActivePage("PlayerSettings")}>Next</Button>
    </Container>
  )
}

export default Instructions;