import axios from "axios";
import { API_URL_BASE } from "./api-url-config";

const API_URL = API_URL_BASE

export async function fetch_screening_info(screeningId){
    return axios.get(API_URL + "screenings/" + screeningId, {}, {}).catch(err => console.warn(err))
}

export async function fetch_screening_seats(screeningId){
    return axios.get(API_URL + "screenings/" + screeningId + "/seats" , {}, {}).catch(err => console.warn(err))
}