import { useContext } from "react";
import GameContext from "../Context/GameProvider";

export default function useGame(){
    return useContext(GameContext)
}