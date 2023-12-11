import React, {useState} from 'react'
import { Container } from 'react-bootstrap'
import Instructions from '../Components/Instructions'
import GameSettings from '../Components/GameSettings'
import PlayerSettings from '../Components/PlayerSettings'
import SecretSetter from '../Components/SecretSetter'

function Settings() {
    const [activePage, setActivePage] = useState("Instructions");

    function getActivePage() {
        switch(activePage) {
            case 'Instructions': return <Instructions setActivePage={setActivePage}/>;
            case 'PlayerSettings': return <PlayerSettings setActivePage={setActivePage}/>;
            case 'GameSettings': return <GameSettings setActivePage={setActivePage}/>;
            case 'SecretSetter': return <SecretSetter setActivePage={setActivePage}/>;
        } 
    }

    return (
        <Container className='vh-100 vw-100 d-flex flex-column'>
            {getActivePage()}
        </Container>
    )
}

export default Settings;