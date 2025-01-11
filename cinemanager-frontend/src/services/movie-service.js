import axios from "axios";
import { API_URL_BASE } from "./api-url-config";
import { getCurrentUser } from "./authentication-service";

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

export async function fetch_rating(id) {
    return axios.get(API_URL + `/${id}/rating`)
        .then(res => {
            return res.data
        }).catch(err => console.warn(err))
}

export async function fetch_reviews(id, page, size) {
    const params = {
        movieId: id,
        page: page,
        size: size
    }

    const queryString = new URLSearchParams(params).toString()

    return axios.get(`${API_URL_BASE}reviews?${queryString}`, {}, {})
    .then(res => {
        return res.data;
    })
    .catch(err => {console.warn(err)})
}

export async function post_review(id, content, rating) {
    return axios.post(`${API_URL_BASE}reviews`, {
        content: content,
        rating: rating,
        movieId: id,
    }, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    })
}

export async function delete_review(id) {
    return axios.delete(`${API_URL_BASE}reviews/${id}`, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    })
}

export async function update_review(reviewId, movieId, content, rating) {
    return axios.put(`${API_URL_BASE}reviews/${reviewId}`, {
        content: content,
        rating: rating,
        movieId: movieId,
    }, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    })
}