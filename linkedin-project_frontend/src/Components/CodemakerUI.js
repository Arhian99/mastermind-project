import React, { useEffect, useState, useRef } from 'react'
import { Container, Row, Col, Button, Alert} from 'react-bootstrap'
import useGame from '../Hooks/useGame';
import axios from '../API/axios';
import GameOverModal from '../Components/GameOverModal';
import { useNavigate } from 'react-router-dom';
import InstructionsModal from '../Components/InstructionsModal';
import SecretModal from '../Components/SecretModal';

function CodemakerUI() {
    const {currentPlayer, gameSession, setGameSession} = useGame();
    const [errorMsg, setErrorMsg] = useState(null);
    const [showGameOverModal, setShowGameOverModal] = useState(gameSession?.gameOver);
    const navigate = useNavigate();
    const [showInstructionsModal, setShowInstructionsModal] = useState(false);
    const [showSecretSetterModal, setShowSecretSetterModal] = useState(gameSession?.secret===null);
    const interval = useRef();

    async function getSession(){
        try {
            const response = await axios.get(
                "/game/session?sessionID="+gameSession?.sessionID+"&username="+currentPlayer.username
            )
            
            setGameSession(response?.data);
            setShowGameOverModal(response?.data?.gameOver);
            setShowSecretSetterModal(response?.data?.secret===null);

        } catch(e) {
            setErrorMsg(e?.response?.data)
        }
    }

    useEffect(() => {
        interval.current = setInterval(() => {
            console.log("retrieving data...");
            getSession();
        }, 1000*15)

        return () => clearInterval(interval.current);

    }, [gameSession]);

    return (
        <Container className='min-vh-100 min-vw-100 d-flex flex-column align-items-center p-5 pt-0'>
            <h1>Game</h1>

            <Container className='border border-4 border-bottom-0 d-flex py-5 rounded-top'>
                <Container className='border border-3 border-top-0 border-bottom-0 border-start-0 m-0 p-0'>
                    <h5>Session ID: {gameSession?.sessionID}</h5>
                    <h5 className='bg-success border rounded border-success py-1 bg-opacity-50 text-white'>Codemaker: {gameSession?.codemaker===null ? "Waiting to join...": gameSession?.codemaker}</h5>
                    <h5>Codebreaker: {gameSession?.codebreaker===null ? "Waiting to join..." : gameSession?.codebreaker}</h5>
                    <h5>Guesses Left: {parseInt(gameSession?.gameSettings?.guessLimit) - parseInt(gameSession?.guessNumb)}</h5>
                </Container>

                <Container className='d-flex flex-column justify-content-center align-items-center'>
                    <Button onClick={() => navigate("/")} className='mb-2 w-80 fw-semibold bg-black border-black'>New Game</Button>
                    <Button onClick={() => setShowInstructionsModal(true)} className='w-80 fw-semibold bg-black border-black'>Instructions</Button>
                </Container>
            </Container>

            <Container className=' border border-4 border-top-0 rounded flex-grow-1'>
                <Row className='bg-secondary-subtle border border-dark border-3 p-0 m-0 rounded-top'>
                    <Col className='p-0 m-0 text-center fs-3 fw-bold'># of guess</Col>
                    <Col className='p-0 m-0 text-center fs-3 fw-bold'>Guess</Col>
                    <Col className='p-0 m-0 text-center fs-3 fw-bold'>Feedback</Col>
                </Row>

                {gameSession?.guesses?.map( guess => {
                    return (
                        <Row className='bg-secondary-subtle p-0 m-0 border border-dark align-items-center' key={guess.index}>
                            <Col className='p-0 m-0 text-center fs-5 fw-medium'>{guess.index}</Col>
                            <Col className='p-0 m-0 text-center fs-5 fw-medium'>{guess.guess}</Col>
                            <Col className='p-0 m-0 text-center fs-5 fw-medium'>{<div dangerouslySetInnerHTML={{__html: guess.feedback}}></div>}</Col>
                        </Row>
                    )
                })}

                {errorMsg === null ? null : <Alert variant='danger'>{errorMsg}</Alert>}
            </Container>

            <GameOverModal 
                show={showGameOverModal}
                onYesClick={() => navigate("/settings")}
                onNoClick={() => setShowGameOverModal(false)}
            />

            <SecretModal 
                show={showSecretSetterModal}
                setShowSecretSetterModal={setShowSecretSetterModal}
            />
            
            <InstructionsModal 
                show={showInstructionsModal}
                handleClose={() => setShowInstructionsModal(false)}
            />
        </Container>
    )
}

export default CodemakerUI;