
export default class GameSettingsDTO{
    numbOfPlayers;
    repeatedDigits;
    difficulty;

    secretLength;       // set in the backend
    secretMax;          // set in the backend
    guessLimit;         // set in the backend

    constructor(numbOfPlayers, repeatedDigits, difficulty) {
        this.numbOfPlayers = numbOfPlayers;
        this.repeatedDigits = repeatedDigits;
        this.difficulty = difficulty;
    }

    static build(numbOfPlayers, repeatedDigits, difficulty){
        return new GameSettingsDTO(numbOfPlayers, repeatedDigits, difficulty);
    }
}