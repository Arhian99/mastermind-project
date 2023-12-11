import React from 'react'
import { Modal } from 'react-bootstrap'
import {Button} from 'react-bootstrap'
import useGame from '../Hooks/useGame';

function InstructionsModal(props) {
    const {gameSession, currentPlayer} = useGame();

    function getPlayerRole() {
        if(currentPlayer?.username===gameSession?.codebreaker) return "Codebreaker";
        else if(currentPlayer?.username===gameSession?.codemaker) return "Codemaker";
    }

    function getMessage(){
        if(getPlayerRole()==="Codebreaker"){
            return "You are the codebreaker, your job is to guess the secret code in the least number of guesses possible. For every guess the system will generate some feedback that states the number of correct values in your guess and how many of those correct values are in the correct positions. You win if you can guess all correct numbers and their correct positions before you run out of guesses. You loose otherwise."
        }

        if(getPlayerRole()==="Codemaker"){
            return "You are the codemaker, your job is to create a secret code that your opponent (the codebreaker) cannot guess. However, the secret code must follow the game settings that were agreed upon before the start of the game. You win if the codebreaker fails to guess the secret code before they run out of guesses. You loose otherwise."
        }
    }

    return (
        <Modal size="lg" centered {...props}>
            <Modal.Header>
                <Modal.Title>Instructions</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <h6>Game Settings</h6>
                <ul>
                    <li>Repeated Digits: {gameSession?.gameSettings?.repeatedDigits === true ? "On" : "Off"}</li>
                    <li>Secret Length: {gameSession?.gameSettings?.secretLength}</li>
                    <li>Secret Digits Range: 0-{gameSession?.gameSettings?.secretMax}</li>
                    <li>Remember: Only numbers allowed in secret!</li>
                </ul>
                <p>{getMessage()}</p>
            </Modal.Body>

            <Modal.Footer>
                <Button onClick={props.handleClose}>Ok</Button>
            </Modal.Footer>
        </Modal>
    )
}

export default InstructionsModal;