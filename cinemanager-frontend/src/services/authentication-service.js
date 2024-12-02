import axios from "axios";

const API_URL = " http://localhost:8080/api/";

export function login(email, password){
    return axios.post(API_URL + "token", {}, {
        headers: {
            Authorization: "Basic " + btoa(email + ":" + password)
    
        }
    }).then(res => {
        if (res.data) {
            // SAVE USER INFO TO LOCALSTORAGE FIELD
            localStorage.setItem("user", JSON.stringify(res.data));
        }
        return res.data;
    })
}

export function logout(){
    localStorage.removeItem("user");
}

export function register(email, firstName, lastName, password, role){
    return axios.post(API_URL + "register", {
        email: email,
        firstName: firstName,
        lastName: lastName,
        password: password,
        role: role
    },
    {
        headers: {
            'Content-Type': 'application/json',
    
        }
    }).then(res => {
        console.log(res)
    }).catch(error => {
        console.log(error)
    })
}

export function add_user(email, firstName, lastName, password, role){
    return axios.post(API_URL + "register", {
        email: email,
        firstName: firstName,
        lastName: lastName,
        password: password,
        role: role
    },
    {
        headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + JSON.parse(localStorage.getItem("user"))
    
        }
    }).then(res => {
        console.log(res)
    }).catch(error => {
        console.log(error)
    })
}

export function getCurrentUser(){
    // GET USER INFO FROM LOCALSTORAGE FIELD
    return JSON.parse(localStorage.getItem("user"));
}