
export default class PlayerDTO{
    username;
    password;

    constructor(username, password){
        this.username = username;
        this.password = password;
    }

    static build(username, password){
        return new PlayerDTO(username, password);
    }
}