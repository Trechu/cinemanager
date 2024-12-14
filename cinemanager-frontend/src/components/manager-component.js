import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import { useEffect, useState } from "react";
import { delete_user, fetch_users } from "../services/user-service";
import { getCurrentUser, add_user } from "../services/authentication-service";
import isEmail from "validator/lib/isEmail";

const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                To pole jest wymagane do usunięcia użytkownika
            </div>
        );
    }
}

const email_valid = value => {
    if (!isEmail(value)) {
        return (
            <div className="alert alert-danger" role="alert">
                Podany email jest nieprawidłowy
            </div>
        );
    }
}

export default function Manager() {
    const [id, setId] = useState();
    const [form, setForm] = useState();
    const [checkBtn, setCheckBtn] = useState();
    const [content, setContent] = useState([]);

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [role, setRole] = useState("CUSTOMER");


    const user = getCurrentUser();
    const [currentUser, setCurrentUser] = useState(user ? JSON.parse(atob(user.split('.')[1])) : null);
    const hasAccess = (currentUser.scope == "ROLE_MANAGER" || currentUser.scope == "ROLE_ADMINISTRATOR");

    function onChangeEmail(e) {
        setEmail(e.target.value);
    }
    function onChangePassword(e) {
        setPassword(e.target.value);
    }
    function onChangeFirstName(e) {
        setFirstName(e.target.value);
    }
    function onChangeLastName(e) {
        setLastName(e.target.value);
    }
    function onChangeRole() {
        setRole(document.getElementById("roleSelect").value);
    }
    function onChangeId(e) {
        setId(e.target.value);
    }

    function handleAddUser(e) {
        e.preventDefault();

        form.validateAll();

        if (checkBtn.context._errors.length === 0) {
            add_user(email, firstName, lastName, password, role).then(
                () => {
                    alert("Rejestracja użytkownika powiodła się");
                    window.location.reload();
                },
                error => {
                    alert("Rejestracja użytkownika nie powiodła się");
                }
            )
        }
    }

    function handleRemoveUser(e){
        e.preventDefault();

        delete_user(id).then(
            () => {
                alert("Usunięcie użytkownika powiodło się");
                window.location.reload();
            },
            error => {
                alert("Usunięcie użytkownika nie powiodło się");
            }
        )
    }

    useEffect(() => {
        async function show_users() {
            setContent(await fetch_users());
        }
        show_users();
    })

    return (
        (hasAccess && content != undefined &&
            (
                <div>
                    <div className="col-md-12">
                        <div className="card card-container">
                            <img
                                style={{ "alignSelf": "center" }}
                                src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
                                alt="profile-img"
                                className="profile-img-card"
                                width="300"
                                height="300"
                            />

                            <Form
                                onSubmit={handleAddUser}
                                ref={c => {
                                    setForm(c);
                                }}
                            >
                                <div>

                                    <div className="form-group">
                                        <label htmlFor="email">Email</label>
                                        <Input
                                            type="text"
                                            className="form-control"
                                            name="email"
                                            value={email}
                                            onChange={onChangeEmail}
                                            validations={[required, email_valid]}
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="firstname">Imię</label>
                                        <Input
                                            type="text"
                                            className="form-control"
                                            name="firstname"
                                            value={firstName}
                                            onChange={onChangeFirstName}
                                            validations={[required]}
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="lastname">Nazwisko</label>
                                        <Input
                                            type="text"
                                            className="form-control"
                                            name="lastname"
                                            value={lastName}
                                            onChange={onChangeLastName}
                                            validations={[required]}
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="password">Hasło</label>
                                        <Input
                                            type="password"
                                            className="form-control"
                                            name="password"
                                            value={password}
                                            onChange={onChangePassword}
                                            validations={[required]}
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="Role">Rola</label>
                                        <select id="roleSelect" defaultValue="CUSTOMER" onChange={onChangeRole}>
                                            <option value="CUSTOMER">Klient</option>
                                            <option value="EMPLOYEE">Pracownik</option>
                                            <option value="MANAGER">Manager</option>
                                            <option value="ADMINISTRATOR">Admin</option>
                                        </select>
                                    </div>

                                    <div className="form-group">
                                        <button className="btn btn-primary btn-block">Dodaj użytkownika</button>
                                    </div>
                                </div>

                                <CheckButton
                                    style={{ display: "none" }}
                                    ref={c => {
                                        setCheckBtn(c);
                                    }}
                                />
                            </Form>
                        </div>
                    </div>
                    <h1>USERS</h1>
                    {content.map((user) => (
                        <h3 key={user.id}>{"User id: " + user.id + " user email: " + user.email + " user role: " + user.role}</h3>
                    ))}

                    <Form
                        onSubmit={handleRemoveUser}
                    >
                        <div>
                            <div className="form-group">
                                <label htmlFor="id">ID użytkownika</label>
                                <Input
                                    type="text"
                                    className="form-control"
                                    name="id"
                                    value={id}
                                    onChange={onChangeId}
                                />
                            </div>

                            <div className="form-group">
                                <button className="btn btn-primary btn-block">Usuń użytkownika</button>
                            </div>
                        </div>

                        <CheckButton
                            style={{ display: "none" }}
                        />
                    </Form>
                </div>
            )
        )
    )

}