import { currentUser } from "./authentication-service";

export default function authHeader(){
    const user = currentUser();

    if( user && user.accessToken){
        return {Authorization: "Bearer " + user.accessToken};
    }

    return {}
}