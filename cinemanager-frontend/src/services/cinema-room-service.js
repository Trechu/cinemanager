import axios from "axios";
import { API_URL_BASE } from "./api-url-config";

const API_URL = API_URL_BASE + 'cinema-rooms'

export async function fetch_cinema_rooms(){
    return axios.get(API_URL, {}, {})
    .then(res => {
        return res.data;
    })
    .catch(err => {console.warn(err)})
}