import React from 'react'
import useGame from '../Hooks/useGame';
import CodebreakerUI from '../Components/CodebreakerUI';
import CodemakerUI from '../Components/CodemakerUI';
import { Alert, Container, Button} from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

function Game() {
    const {currentPlayer, gameSession} = useGame();
    const navigate = useNavigate();

    function getPage() {
        switch(currentPlayer?.username){
            case `${gameSession?.codebreaker}`: return <CodebreakerUI />;
            case `${gameSession?.codemaker}`: return <CodemakerUI />;
            default: return (
                <Container className='vh-100 vw-100 d-flex flex-column align-items-center justify-content-center'>
                    <Alert variant='danger' className='h-25 w-50 text-center fs-2 fw-bold'>Something went wrong...</Alert>
                    <Button onClick={() => navigate("/")}> Go Home </Button>
                </Container>
            );   
        };
    }

    return (
        <>
            {getPage()}
        </>
    )
}

export default Game;