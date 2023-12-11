import React from 'react'
import { Button, Modal } from 'react-bootstrap'
import useGame from '../Hooks/useGame';

function GameOverModal(props) {
    const {gameSession} = useGame();

    return (
        <Modal size="lg" centered {...props}>
            <Modal.Header>
                <Modal.Title >Game Over!</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <h4>{gameSession?.winner} is the winner!</h4>
                <h5>Secret was: {gameSession?.secret}</h5>
                <p>Would you like to play again?</p>
            </Modal.Body>

            <Modal.Footer>
                <Button onClick={props.onYesClick}>Yes</Button>
                <Button onClick={props.onNoClick}>No</Button>
            </Modal.Footer>
        </Modal>
    )
}

export default GameOverModal;