import { createContext, useEffect, useState } from "react";

const GameContext = createContext({})

export const GameProvider = ({ children }) => {
    
    const [currentPlayer, setCurrentPlayer] = useState(() => {
        if(window.localStorage.getItem("currentPlayer") === null) return null;
        return JSON.parse(window.localStorage.getItem("currentPlayer"));
    });

    const [gameSession, setGameSession] = useState(() => {
        if(window.localStorage.getItem('gameSession') === null) return null;
        return JSON.parse(window.localStorage.getItem('gameSession'));
    });
    
    useEffect(() => {
        window.localStorage.setItem('gameSession', JSON.stringify(gameSession));
        window.localStorage.setItem('currentPlayer', JSON.stringify(currentPlayer));
    }, [gameSession, currentPlayer])

    return (
        <GameContext.Provider value={{gameSession, setGameSession, currentPlayer, setCurrentPlayer}}>
            {children}
        </GameContext.Provider>
    )
}

export default GameContext;