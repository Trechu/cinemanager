import axios from "axios";
import authHeader from "./authentication-header";

const API_URL = "";

export default function getContent(){
    return axios.get(API_URL , {
        headers: authHeader()
    });
}