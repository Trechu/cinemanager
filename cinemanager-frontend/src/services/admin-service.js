import { API_URL_BASE } from "./api-url-config"
import axios from "axios"

const API_URL = API_URL_BASE

export function admin_func(){
    return axios.post(API_URL + "orders", {
        "screeningId": 1,
        "tickets": [
            {
                "row": 1,
                "seatNumber": 5,
                "ticketType": "DISCOUNTED"
            },
            {
                "row": 2,
                "seatNumber": 7,
                "ticketType": "REGULAR"
            }
        ]
    },
    {
        headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + JSON.parse(localStorage.getItem("user"))
        }
    })
}