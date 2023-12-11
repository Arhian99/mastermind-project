import React, { useState } from 'react'
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { Container, Form, Button, Alert } from 'react-bootstrap';
import useGame from '../Hooks/useGame';
import { useNavigate } from 'react-router-dom';
import GameSessionDTO from '../DTOs/GameSessionDTO';

import axios from '../API/axios';
import BackendDTO from '../DTOs/BackendDTO';

function SecretSetter({setActivePage}) {
    const {setGameSession, gameSession, setCurrentPlayer, currentPlayer} = useGame();
    const [errorMsg, setErrorMsg] = useState(null);
    const navigate = useNavigate();

    const formik = useFormik({
        initialValues: {
            secret: "",
        },
        validationSchema: Yup.object({
            secret: Yup.string().required("Secret is required")
        }),
        onSubmit: async (values) => {
            let gameSessionDTO = GameSessionDTO.build(
                gameSession?.sessionID,             // sessionID
                gameSession?.isMultiplayer,         // isMultiplayer
                gameSession?.codebreaker,           // codebreaker
                gameSession?.codemaker,             // codemaker
                gameSession?.guessNumb,             // guessNumb                
                gameSession?.currentGuess,          // currentGuess
                gameSession?.guesses,               // guesses
                gameSession?.gameSettings,          // gameSettings
                gameSession?.roundNumb,             // roundNumb
                gameSession?.roundOver,             // roundOver
                gameSession?.gameOver,              // gameOver
                gameSession?.winner,                // winner
                values.secret                       // secret
            )

            let backendDTO = BackendDTO.build(
                gameSessionDTO,
                currentPlayer
            )
            setGameSession(gameSessionDTO)

            try {
                const response = await axios.post(
                    "/game/new",
                    backendDTO
                )

                setGameSession(response?.data?.currentSessionDTO)
                setCurrentPlayer(response?.data?.currentPlayer)
                navigate("/game");

            } catch(error){
                setErrorMsg(error?.response?.data)
            }
        }
    })

    return (
        <Container className='mt-5 border border-4 rounded h-80 w-80' >
            <h1 className='mb-4 '>{`For the codemaker ( ${gameSession?.codemaker} ) eyes only!`}</h1>

            <Form style={{width: "350px"}} onSubmit={formik.handleSubmit}>
                <Form.Group className="my-3" controlId="secret">
                    <Form.Label>Enter Secret: </Form.Label>
                    <Form.Control type="password"
                                name="secret"
                                onChange={formik.handleChange}
                                onBlur={formik.handleBlur}
                                value={formik.values.secret}
                    />
                    <Form.Text className='text-danger'>
                        {formik.touched.secret && formik.errors.secret ? (
                            <div className='text-danger'>{formik.errors.secret}</div>
                        ) : null}
                    </Form.Text> 
                </Form.Group>

                <Button type='button' onClick={() => setActivePage("GameSettings")} className="bg-black border-black fw-semibold">Back</Button>
                <Button type="submit" className="mx-3 bg-black border-black fw-semibold">Begin</Button>

                {errorMsg === null ? null : <Alert variant='danger' className='my-3'>{errorMsg}</Alert>}
            </Form>
        </Container>
    )
}

export default SecretSetter;