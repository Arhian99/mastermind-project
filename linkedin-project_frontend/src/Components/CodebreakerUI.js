import React, { useEffect, useState } from 'react'
import { Container, Row, Col, Button, Form, Alert} from 'react-bootstrap'
import * as Yup from 'yup';
import { useFormik } from 'formik';
import useGame from '../Hooks/useGame';
import axios from '../API/axios';
import GameOverModal from '../Components/GameOverModal';
import { useNavigate } from 'react-router-dom';
import InstructionsModal from '../Components/InstructionsModal';
import BackendDTO from '../DTOs/BackendDTO';
import UnsetSecretModal from './UnsetSecretModal';

function CodebreakerUI() {
    const {currentPlayer, gameSession, setGameSession} = useGame();
    const [errorMsg, setErrorMsg] = useState(null);
    const [showGameOverModal, setShowGameOverModal] = useState(gameSession?.gameOver);
    const navigate = useNavigate();
    const [showInstructionsModal, setShowInstructionsModal] = useState(false);
    const [showNullSecretModal, setShowNullSecretModal] = useState(gameSession?.secret===null);

    async function getSession(){
        try {
            const response = await axios.get(
                "/game/session?sessionID="+gameSession?.sessionID+"&username="+currentPlayer.username
            )
            
            setGameSession(response?.data);
            setShowGameOverModal(response?.data?.gameOver);
            setShowNullSecretModal(response?.data?.secret===null);

        } catch(e) {
            setErrorMsg(e?.response?.data)
        }
    }

    useEffect(() => {
        getSession();
    }, []);


    const formik = useFormik({
        initialValues: {
            guess:''
        },
        validationSchema: Yup.object({
            guess: Yup.string().required("Cannot submit an empty guess.")
        }),
        onSubmit: async (values, {resetForm}) => {
            setErrorMsg(null);
            
            let backendDTO = BackendDTO.build(
                {
                    "sessionID": gameSession?.sessionID,
                    "currentGuess": {
                        "index": null,
                        "guess":values?.guess,
                        "feedback": null
                    }
                },
                currentPlayer
            )

            try{
                const response = await axios.post(
                    "/game/postGuess",
                    backendDTO
                )
                
                setGameSession(response?.data);
                setShowGameOverModal(response?.data?.gameOver);
                setShowNullSecretModal(response?.data?.secret===null);

            } catch(error){
                setErrorMsg(error?.response?.data)
            }

            resetForm();
        }
    })

    return (
        <Container className='min-vh-100 min-vw-100 d-flex flex-column align-items-center p-5 pt-0'>
            <h1>Game</h1>

            <Container className='border border-4 border-bottom-0 d-flex py-5 rounded-top'>
                <Container className='border border-3 border-top-0 border-bottom-0 border-start-0 m-0 p-0'>
                    <h5>Session ID: {gameSession?.sessionID}</h5>
                    <h5>Codemaker: {gameSession?.codemaker===null ? "Computer": gameSession?.codemaker}</h5>
                    <h5 className='bg-success border rounded border-success py-1 bg-opacity-50 text-white'>Codebreaker: {gameSession?.codebreaker===null ? "Waiting to join..." : gameSession?.codebreaker}</h5>
                    <h5>Guesses Left: {parseInt(gameSession?.gameSettings?.guessLimit) - parseInt(gameSession?.guessNumb)}</h5>
                </Container>

                <Container className='d-flex flex-column justify-content-center align-items-center'>
                    <Button onClick={() => navigate("/")} className='mb-2 w-80 fw-semibold bg-black border-black'>New Game</Button>
                    <Button onClick={() => setShowInstructionsModal(true)} className='w-80 fw-semibold bg-black border-black'>Instructions</Button>
                </Container>
            </Container>

            <Container className=' border border-4 border-top-0 border-bottom-0 flex-grow-1'>
                <Row className='bg-secondary-subtle p-0 m-0 border-dark border-3 rounded-top'>
                    <Col className='p-0 m-0 text-center fs-3 fw-bold'># of guess</Col>
                    <Col className='p-0 m-0 text-center fs-3 fw-bold'>Guess</Col>
                    <Col className='p-0 m-0 text-center fs-3 fw-bold'>Feedback</Col>
                </Row>

                {gameSession?.guesses?.map( guess => {
                    return (
                        <Row className='bg-secondary-subtle border border-dark p-0 m-0 align-items-center' key={guess.index}>
                            <Col className='p-0 m-0 text-center fs-5 fw-medium'>{guess.index}</Col>
                            <Col className='p-0 m-0 text-center fs-5 fw-medium'>{guess.guess}</Col>
                            <Col className='p-0 m-0 text-center fs-5 fw-medium'>{<div dangerouslySetInnerHTML={{__html: guess.feedback}}></div>}</Col>
                        </Row>
                    )
                })}

                {errorMsg === null ? null : <Alert variant='danger'>{errorMsg}</Alert>}
            </Container>

            <Container className=' border border-4 border-top-0 pb-4 fs-5 rounded-bottom'>
                <Form onSubmit={formik.handleSubmit}>
                    <Form.Group controlId="guess">
                        <Form.Label className='d-inline-flex m-0 p-0 mr-2 fw-medium'>Enter guess: </Form.Label>
                        <Form.Control 
                            type="text"
                            name="guess"
                            placeholder="1234" 
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            value={formik.values.guess}
                            className='d-inline p-0 mx-2 mw-50 fs-5'
                        />

                        <Button type="submit" className='d-inline mx-2 py-0 fw-semibold bg-black fs-5 border-black'>Submit</Button>

                    </Form.Group>
                    <Form.Text className='text-danger bg-danger'>
                            {formik.touched.guess && formik.errors.guess ? (
                                <div className='text-danger'>{formik.errors.guess}</div>
                            ) : null}
                    </Form.Text>
                </Form>
            </Container>

            <GameOverModal 
                show={showGameOverModal}
                onYesClick={() => navigate("/")}
                onNoClick={() => setShowGameOverModal(false)}
            />

            <UnsetSecretModal 
                show={showNullSecretModal}
                setShowNullSecretModal={setShowNullSecretModal}
                getSession={getSession}
            />

            <InstructionsModal 
                show={showInstructionsModal}
                handleClose={() => setShowInstructionsModal(false)}
            />
        </Container>
    )
}

export default CodebreakerUI;