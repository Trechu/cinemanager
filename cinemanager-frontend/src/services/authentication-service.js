import axios from "axios";

const AUTHENTICATION_API = "";

export function login(email, password){
    return axios.post(AUTHENTICATION_API + "login", {
        email,
        password
    })
    .then(res => {
        if (res.data.accessToken) {
            // SAVE USER INFO TO LOCALSTORAGE FIELD
            localStorage.setItem("user", JSON.stringify(res.data));
        }
        return res.data;
    })
}

export function logout(){
    localStorage.removeItem("user");
}

export function register(email, firstName, lastName, password){
    return axios.post(AUTHENTICATION_API + "register", {
        email,
        firstName,
        lastName,
        password
    });
}

export function getCurrentUser(){
    // GET USER INFO FROM LOCALSTORAGE FIELD
    return JSON.parse(localStorage.getItem("user"));
}