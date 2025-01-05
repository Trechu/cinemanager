import axios from "axios";
import { API_STATIC_URL, API_URL_BASE } from "./api-url-config";

const API_URL = API_URL_BASE + 'screenings'

export async function fetch_screenings(page, size){
    return axios.get(API_URL + "?page=" + page + "&size=" + size, {}, {}).catch(err => {console.warn(err)})
}

export async function fetch_movie_poster(posterUrl){
    return axios.get(API_STATIC_URL + posterUrl,{},{}).catch(err => {console.warn(err)})
}