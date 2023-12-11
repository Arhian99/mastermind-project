
export default class GameSessionDTO{
    sessionID;
    isMultiplayer;
    codebreaker;
    codemaker;
    guessNumb;
    currentGuess;
    guesses = [];
    gameSettings = {};
    roundNumb;
    isRoundOver;
    isGameOver;
    winner;
    secret;


    constructor(sessionID, isMultiplayer, codebreaker, codemaker, guessNumb, currentGuess, guesses, gameSettings, roundNumb, isRoundOver, isGameOver, winner, secret) {
        this.sessionID = sessionID;
        this.isMultiplayer = isMultiplayer;
        this.codebreaker = codebreaker;
        this.codemaker = codemaker;
        this.guessNumb = guessNumb;
        this.currentGuess = currentGuess;
        this.guesses = guesses;
        this.gameSettings = gameSettings;
        this.roundNumb = roundNumb;
        this.isRoundOver = isRoundOver;
        this.isGameOver = isGameOver;
        this.winner = winner;
        this.secret = secret;
    }

    static build(sessionID, isMultiplayer, codebreaker, codemaker, guessNumb, currentGuess, guesses, gameSettings, roundNumb, isRoundOver, isGameOver, winner, secret) {
        return new GameSessionDTO(sessionID, isMultiplayer, codebreaker, codemaker, guessNumb, currentGuess, guesses, gameSettings, roundNumb, isRoundOver, isGameOver, winner, secret);
    }
}