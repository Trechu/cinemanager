import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import { useEffect, useState } from "react";
import { fetch_users } from "../services/user-service";

const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                To pole jest wymagane do usunięcia użytkownika
            </div>
        );
    }
}

export default function Manager(){
    const [id, setId] = useState();
    const [form, setForm] = useState();
    const [checkBtn, setCheckBtn] = useState();
    const [content, setContent] = useState([]);

    function handleLogin(e) {
        e.preventDefault();

        form.validateAll();

        if (checkBtn.context._errors.length === 0) {
        }
    }

    useEffect(() => {
        async function show_users() {
            setContent(await fetch_users());
        }
        show_users();
    })

    return (
        <div>
            {content.map((user) => (
                <h3 key={user.id}>{"User id: " + user.id + " user email: " + user.email + " user role: " + user.role}</h3>
            ))}
        </div>
    )

}