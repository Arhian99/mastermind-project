import React, {useState} from 'react'
import { Container, Form, FloatingLabel, Button, Alert } from 'react-bootstrap';
import * as Yup from 'yup';
import { useFormik } from 'formik';
import useGame from '../Hooks/useGame';
import axios from '../API/axios';

function PlayerSettings({setActivePage}) {
    const {setCurrentPlayer} = useGame();
    const [errorMsg, setErrorMsg] = useState(null);

    const formik = useFormik({
        initialValues: {
            username: "",
            password: ""
        },
        validationSchema: Yup.object({
            username: Yup.string().required("Please enter a username."),
            password: Yup.string().max(12).required("Please enter a password."),
        }),
        onSubmit: async (values) => {
            try {
                const response = await axios.post(
                    "/authenticate",
                    {},
                    {
                        
                        headers: {
                            'Content-Type': 'application/json',
                            "Authorization" : "Basic "+values.username+":"+values.password
                        }
                    }
                )

                setCurrentPlayer(response?.data)
                setActivePage("GameSettings")

            } catch(error){
                setErrorMsg(error?.response?.data)
            }
        }
    })


    return (
        <Container className='mt-5 border border-4 rounded h-80 w-80' >
            <h1 className='mb-4 '>Player Settings</h1>

            <Form style={{width: "350px"}} onSubmit={formik.handleSubmit}>

                <Form.Group className="my-3" controlId="username">
                    <Form.Label>Enter Username: </Form.Label>
                    <FloatingLabel
                        controlId="username"
                        label="Username"
                        className="mb-3"
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

                <Form.Group className="my-3" controlId="password">
                    <Form.Label>Enter Password: </Form.Label>
                    <FloatingLabel
                        controlId="password"
                        label="Password"
                        className="mb-3"
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

                <Button onClick={() => setActivePage("Instructions")} type='button' className='fw-semibold bg-black border-black'>Back</Button>
                <Button type="submit" className="mx-3 fw-semibold bg-black border-black">Next</Button>

                {errorMsg === null ? null : <Alert variant='danger' className="my-3">{errorMsg}</Alert>}

            </Form>
        </Container>
    )
}

export default PlayerSettings;