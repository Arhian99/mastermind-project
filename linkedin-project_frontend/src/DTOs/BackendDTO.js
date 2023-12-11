
export default class BackendDTO{
    currentSessionDTO={}
    currentPlayer={}

    constructor(currentSessionDTO, currentPlayer){
        this.currentSessionDTO = currentSessionDTO;
        this.currentPlayer = currentPlayer;
    }

    static build(currentSessionDTO, currentPlayer){
        return new BackendDTO(currentSessionDTO, currentPlayer);
    }
}