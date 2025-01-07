import axios from "axios";
import { API_URL_BASE } from "./api-url-config";

const API_URL = API_URL_BASE + 'movies'

export async function fetch_movies(page, size, genre){
    const params = {
        page: page,
        size: size,
        genre: genre
    }

    const queryString = new URLSearchParams(params).toString()

    return axios.get(`${API_URL}?${queryString}`, {}, {})
    .then(res => {
        return res.data;
    })
    .catch(err => {console.warn(err)})
}

export async function fetch_movie(id) {
    return axios.get(API_URL + `/${id}`, {
    }).then(res => {
        return res.data;
    }).catch(err => console.warn(err))
}