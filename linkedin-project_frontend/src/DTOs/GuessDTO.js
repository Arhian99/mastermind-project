
export default class GuessDTO{
    index;
    guess;
    feedback;

    constructor(index, guess, feedback) {
        this.index = index;
        this.guess = guess;
        this.feedback = feedback;
    }

    static build(index, guess, feedback){
        return new GuessDTO(index, guess, feedback);
    }
}