import axios from "axios";
import { API_URL_BASE } from "./api-url-config";
import { getCurrentUser } from "./authentication-service";

const API_URL = API_URL_BASE

export async function fetch_screening_info(screeningId){
    return axios.get(API_URL + "screenings/" + screeningId, {}, {}).catch(err => console.warn(err))
}

export async function fetch_screening_seats(screeningId){
    return axios.get(API_URL + "screenings/" + screeningId + "/seats" , {}, {}).catch(err => console.warn(err))
}

export function place_order(tickets, screeningId){
    const ticketsDto = []
    for(var ticket of tickets){
        ticketsDto.push(
            {
                "row": ticket.row,
                "seatNumber": ticket.position,
                "ticketType": ticket.type=="Ulgowy"?"DISCOUNTED":"REGULAR"
            }
        )
    }
    return axios.post(API_URL + "orders", {
        "screeningId": screeningId,
        "tickets": ticketsDto
    }, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).catch(err => console.warn(err))
}