import React, { useEffect, useRef } from 'react'
import { Modal } from 'react-bootstrap';
import useGame from '../Hooks/useGame';

function UnsetSecretModal(props) {
    const {gameSession} = useGame();
    const interval = useRef();

    useEffect(() => {
        if(gameSession?.secret===null){
            interval.current = setInterval(() => {
                console.log("retrieving data...");
                props.getSession()
            }, 1000*15)
        }

        return () => clearInterval(interval.current);
    }, [gameSession])

    return (
        <Modal size="lg" centered {...props}>
            <Modal.Header>
                <Modal.Title >Secret has not been set by the Codemaker!</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <h3>Waiting for codemaker to set the secret...</h3>
                <p>The page refreshes every 20 seconds!</p>
            </Modal.Body>
        </Modal>
    )
}

export default UnsetSecretModal;
