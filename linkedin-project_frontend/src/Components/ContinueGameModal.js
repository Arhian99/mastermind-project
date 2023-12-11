import { useFormik } from 'formik'
import React, { useState } from 'react'
import { Modal, Button, Form, FloatingLabel, Alert, Container } from 'react-bootstrap'
import * as Yup from 'yup';
import axios from '../API/axios';
import { useNavigate } from 'react-router-dom';
import useGame from '../Hooks/useGame';

function ContinueGameModal(props) {
    const [errorMsg, setErrorMsg] = useState(null);
    const navigate = useNavigate();
    const {setGameSession, setCurrentPlayer} = useGame();

    const formik = useFormik({
        initialValues: {
            username: "",
            password: "",
            players: "1",
            sessionID: ""
        },
        validationSchema: Yup.object({
            username: Yup.string().required("Please enter a username."),
            password: Yup.string().max(12).required("Please enter a password"),
            players: Yup.string().oneOf(["1", "2"], "Please select 1 or 2 players.").required("Please select number of players."),
            sessionID: Yup.string().required("Please enter a session ID.")
        }),
        onSubmit: async (values) => {
            try{
                const response = await axios.get(
                    getURL(),
                    {
                        headers: {
                            "Authorization": "Basic " + values.username+":"+values.password
                        }
                    }
                )
                setGameSession(response?.data?.currentSessionDTO);
                setCurrentPlayer(response?.data?.currentPlayer);

                console.log(response);

                navigate("/game")

            }catch(error) {
                setErrorMsg(error?.response?.data);
            }
        }
    })

    function getURL() {
        if(formik.values.players === "1") return "/authenticate/continueSession?sessionID="+formik.values.sessionID+"&isMultiplayer="+false;
        if(formik.values.players === "2") return "/authenticate/continueSession?sessionID="+formik.values.sessionID+"&isMultiplayer="+true;
    }

    return (

        <Modal size="lg" centered {...props}>
            <Modal.Header>
                <Modal.Title >Continue Game</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <Form onSubmit={formik.handleSubmit} >
                    <Form.Group controlId="username">
                        <Form.Label>Enter Username: </Form.Label>
                        <FloatingLabel
                            controlId="username"
                            label="Username"
                            className="mb-1"
                        >
                            <Form.Control type="text"
                                        name="username"
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        value={formik.values.username}
                            />
                        </FloatingLabel>
                        <Form.Text className='text-danger bg-danger'>
                                {formik.touched.username && formik.errors.username ? (
                                    <div className='text-danger'>{formik.errors.username}</div>
                                ) : null}
                        </Form.Text>
                    </Form.Group>
                                    
                    <Form.Group controlId="password">
                        <Form.Label>Enter Password: </Form.Label>
                        <FloatingLabel
                            controlId="password"
                            label="Password"
                            className="mb-1"
                        >
                            <Form.Control type="password"
                                        name="password"
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        value={formik.values.password}
                            />
                        </FloatingLabel>
                        <Form.Text className='text-danger bg-danger'>
                                {formik.touched.password && formik.errors.password ? (
                                    <div className='text-danger'>{formik.errors.password}</div>
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
                            defaultChecked={formik.values.players=== "1"}
                        />
                        <Form.Check
                            type={"radio"}
                            label={'Two Players'}
                            id={"twoPlayers"}
                            name="players"
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            value="2"
                            defaultChecked={formik.values.players=== "2"}
                        />
                    </Form.Group>


                    <Form.Group controlId="sessionID">
                        <Form.Label>Enter Room Name: </Form.Label>
                        <FloatingLabel
                            controlId="sessionID"
                            label="Session ID"
                            className="mb-1"
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

                    {errorMsg === null ? null : <Alert variant='danger' className="my-2">{errorMsg}</Alert>}
                    
                    <Container className='my-2 p-0'>
                        <Button onClick={props.onClose} type='button' className='me-3'>Close</Button>
                        <Button type='submit'>Begin</Button>
                    </Container>               
                </Form>
            </Modal.Body>
        </Modal>
    )
}

export default ContinueGameModal;