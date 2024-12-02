import axios from "axios";
import { getCurrentUser } from "./authentication-service";

const API_URL = " http://localhost:8080/api/users";

export function delete_user(id){
    return axios.delete(API_URL + "/" + id, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).then(res => {
        return res.data;
    }).catch(err => console.log(err))
}

export async function fetch_users(){
    return axios.get(API_URL , {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).then(res => {
        return res.data;
    }).catch(err => console.log(err))
}