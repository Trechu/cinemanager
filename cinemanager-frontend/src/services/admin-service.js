import { API_URL_BASE } from "./api-url-config"
import axios from "axios"

const API_URL = API_URL_BASE

export function admin_func(){
    return axios.get(API_URL + "movies/highest-rated", {}, {}).then(data => console.log(data))
}