import axios from "axios";
import { API_URL_BASE } from "./api-url-config";

const API_URL = API_URL_BASE + 'screenings'

export async function fetch_screenings(page, size){
    return axios.get(API_URL + "?page=" + page + "&size=" + size, {}, {}).catch(err => {console.warn(err)})
}