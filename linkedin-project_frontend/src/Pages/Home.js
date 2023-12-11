import React, {useState, useEffect} from 'react'
import { Button, Container } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'
import ContinueGameModal from '../Components/ContinueGameModal';
import useGame from '../Hooks/useGame';

function Home() {
    const navigate = useNavigate();
    const [showModal, setShowModal] = useState(false);
    const {setGameSession, setCurrentPlayer} = useGame();

    useEffect(() => {
        setGameSession(null);
        setCurrentPlayer(null);
    }, [])

    function handleShowModal(){
        setShowModal(true);
    }

    function handleHideModal(){
        setShowModal(false);
    }

    return (
        <Container className='vh-100 vw-100 d-flex flex-column align-items-center justify-content-center'>

            <Container className='d-flex flex-column border border-4 border-dark rounded p-5'>
                <h1 className='text-center'>Welcome to Master Mind!</h1>
                <Button className='my-3 fw-semibold bg-black fs-5 border-black' onClick={() => navigate("/settings")}>New Game</Button>
                <Button className='bg-black fw-semibold fs-5 border-black' onClick={handleShowModal}>Continue Game</Button>
            </Container>

            <ContinueGameModal 
                show={showModal}
                onClose={handleHideModal}
            />

        </Container>
    )
}

export default Home;