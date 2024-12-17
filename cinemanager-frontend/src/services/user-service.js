import axios from "axios";
import { getCurrentUser } from "./authentication-service";
import { API_URL_BASE } from "./api-url-config";

const API_URL = API_URL_BASE + "users";

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