import { useFormik } from 'formik'
import React, { useState } from 'react'
import { Container, Form, Button, FloatingLabel, Alert } from 'react-bootstrap'
import * as Yup from 'yup';
import useGame from '../Hooks/useGame';
import GameSettingsDTO from '../DTOs/GameSettingsDTO';
import GameSessionDTO from '../DTOs/GameSessionDTO';
import { useNavigate } from 'react-router-dom';
import axios from '../API/axios';
import BackendDTO from '../DTOs/BackendDTO';

function GameSettings({setActivePage}) {
    const {currentPlayer, setCurrentPlayer, setGameSession} = useGame();
    const navigate = useNavigate();
    const [errorMsg, setErrorMsg] = useState(null);

    const formik= useFormik({
        initialValues: {
            players: "1",
            role: "codemaker",
            sessionID: "",
            difficulty: "easy",
            repeatedDigits: true,
        },
        validationSchema: Yup.object({
            players: Yup.string().oneOf(["1", "2"], "Please select 1 or 2 players.").required("Please select number of players."),
            role: Yup.string().oneOf(["codemaker", "codebreaker"], "Please select a role."),
            sessionID: Yup.string().required("Please choose a session ID."),
            difficulty: Yup.string().oneOf(["easy", "medium", "hard"], "Please select difficulty.").required("Please select difficulty."),
            repeatedDigits: Yup.boolean().oneOf([true, false], "Please select whether or not the secret should have repeated digits.").required("Please select repeated digits support."),
        }),
        onSubmit: async (values) => {
            let gameSessionDTO = GameSessionDTO.build(
                values.sessionID,                   // sessionID
                isMultiplayer(),                    // isMultiplayer
                getCodebreaker(),                   // codebreaker
                getCodemaker(),                     // codemaker
                0,                                  // guessNumb                
                null,                               // currentGuess
                [],                                 // guesses
                GameSettingsDTO.build(              // {GameSettings}
                    parseInt(values.players),               // numbOfPlayers
                    values.repeatedDigits,                  // repeatedDigits
                    values.difficulty                       // difficulty
                ),
                1,                                 // roundNumb
                false,                             // roundOver
                false,                             // gameOver
                null,                              // winner
                null                               // secret
            )

            let backendDTO = BackendDTO.build(
                gameSessionDTO,
                currentPlayer
            )
            
            setGameSession(gameSessionDTO);

            if(values.players==="1") {
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

            } else if(values.players==="2"){
                if(currentPlayer?.username === getCodemaker()){
                    setActivePage("SecretSetter")

                } else if(currentPlayer?.username === getCodebreaker()){
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
            }
        }
    })

    function getCodebreaker() {
        if(formik.values.players==="1") return currentPlayer?.username;
        else if(formik.values.players==="2") {
            if(formik.values.role==="codebreaker") return currentPlayer?.username;
            else return null;
        }
    }

    function getCodemaker(){
        if(formik.values.players==="1") return null;
        else if(formik.values.players==="2"){
            if(formik.values.role==="codemaker") return currentPlayer?.username;
            else return null;
        }
    }

    function isMultiplayer(){
        if(formik.values.players==="1") return false;
        else if(formik.values.players==="2") return true;
    }

    return (
        <Container className='mt-5 border border-4 rounded h-80 w-80' >
            <h1 className='mb-4 '>Game Settings</h1>

            <Form style={{width: "350px"}} onSubmit={formik.handleSubmit}>
                <Form.Group className="my-3" controlId="sessionID">
                    <Form.Label>Choose Room Name: </Form.Label>
                    <FloatingLabel
                        controlId="sessionID"
                        label="Session ID"
                        className="mb-3"
                    >
                        <Form.Control type="text"
                            name="sessionID"
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            value={formik.values.sessionID}
                        />
                    </FloatingLabel>

                    <Form.Text className='text-danger bg-danger'>
                        {formik.touched.sessionID && formik.errors.sessionID ? (
                            <div className='text-danger'>{formik.errors.sessionID}</div>
                        ) : null}
                    </Form.Text>
                </Form.Group>

                <Form.Group className="mb-2" controlId="players">
                    <Form.Label>Number of Players: </Form.Label>
                    <Form.Check
                        type={"radio"}
                        label={'One Player'}
                        id={"onePlayer"}
                        name="players"
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        value="1"
                        defaultChecked={formik.values.players==="1"}
                    />
                    <Form.Check
                        type={"radio"}
                        label={'Two Players'}
                        id={"twoPlayers"}
                        name="players"
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        value="2"
                        defaultChecked={formik.values.players==="2"}
                    />
                </Form.Group>
                
                {
                    formik.values.players==="2" && (
                        <Form.Group className="mb-2" controlId="role">
                            <Form.Label>Select a role: </Form.Label>
                            <Form.Check
                                type={"radio"}
                                label={'Codemaker'}
                                id={"codemaker"}
                                name="role"
                                onChange={formik.handleChange}
                                onBlur={formik.handleBlur}
                                value="codemaker"
                                defaultChecked={formik.values.role==="codemaker"}
                            />
                            <Form.Check
                                type={"radio"}
                                label={'Codebreaker'}
                                id={"codebreaker"}
                                name="role"
                                onChange={formik.handleChange}
                                onBlur={formik.handleBlur}
                                value="codebreaker"
                                defaultChecked={formik.values.role==="codebreaker"}
                            />
                        </Form.Group>
                    )
                }

                <Form.Group className="my-3" controlId="difficulty">
                    <Form.Label className='d-block'>Difficulty: </Form.Label>
                    <Form.Check
                        type={"radio"}
                        label={'Easy'}
                        id={"easyDifficulty"}
                        name="difficulty"
                        value="easy"
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        defaultChecked={formik.values.difficulty==="easy"}
                    />

                    <Form.Check
                        type={"radio"}
                        label={'Medium'}
                        id={"mediumDifficulty"}
                        name="difficulty"
                        defaultChecked={formik.values.difficulty==="medium"}
                        value="medium"
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        
                    />

                    <Form.Check
                        type={"radio"}
                        label={'Hard'}
                        id={"hardDifficulty"}
                        name="difficulty"
                        value="hard"
                        defaultChecked={formik.values.difficulty==="hard"}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                    />
                </Form.Group>

                <Form.Group className="my-3" controlId="repeatedDigits">
                    <Form.Label>Repeated Digits: </Form.Label>
                    <Form.Check
                        type={"radio"}
                        label={'On'}
                        id={"onRepeatedDigits"}
                        name="repeatedDigits"
                        defaultChecked={formik.values.repeatedDigits=== true}
                        value={true}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                    />
                    <Form.Check
                        type={"radio"}
                        label={'Off'}
                        id={"offRepeatedDigits"}
                        name="repeatedDigits"
                        value={false}
                        defaultChecked={formik.values.repeatedDigits=== false}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                    />
                </Form.Group>

                <Button onClick={() => setActivePage("PlayerSettings")} type="button" className='fw-semibold bg-black border-black'>Back</Button>
                <Button type="submit" className='mx-2 fw-semibold bg-black border-black'>Begin</Button>
                
                {errorMsg === null ? null : <Alert variant='danger'>{errorMsg}</Alert>}
            </Form>
        </Container>
    )
}

export default GameSettings;