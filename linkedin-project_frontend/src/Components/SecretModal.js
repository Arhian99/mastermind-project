import React, { useState } from 'react'
import { useFormik } from 'formik'
import { Modal, Button, Form, Alert } from 'react-bootstrap'
import * as Yup from 'yup';
import axios from '../API/axios';
import useGame from '../Hooks/useGame';
import GameSessionDTO from '../DTOs/GameSessionDTO';
import BackendDTO from '../DTOs/BackendDTO';

function SecretModal(props) {
    const [errorMsg, setErrorMsg] = useState(null);
    const {gameSession, setGameSession, currentPlayer, setCurrentPlayer} = useGame();

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

            setGameSession(gameSessionDTO);

            try {
                const response = await axios.post(
                    "/game/setSecret",
                    backendDTO
                )

                setGameSession(response?.data?.currentSessionDTO);
                setCurrentPlayer(response?.data?.currentPlayer);
                props.setShowSecretSetterModal(response?.data?.currentSessionDTO?.secret===null);

            } catch(error){
                setErrorMsg(error?.response?.data)
            }
        }
    })

    return (
        <Modal size="lg" centered {...props}>
            <Modal.Header>
                <Modal.Title >You are the codemaker!</Modal.Title>
            </Modal.Header>

            <Modal.Body>
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

                    <Button type="submit" className="my-2 bg-black border-black fw-semibold">Submit</Button>

                    {errorMsg === null ? null : <Alert variant='danger'>{errorMsg}</Alert>}
                </Form>

            </Modal.Body>
        </Modal>

    )
}

export default SecretModal;