import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import { login } from "../services/authentication-service";

import { useState } from "react";
import { Navigate } from "react-router-dom";

const required = value => {
    if(!value){
        return (
            <div className="alert alert-danger" role="alert">
              This field is required!
            </div>
          );
    }
}

export default function Login(){

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [form, setForm] = useState();
    const [checkBtn, setCheckBtn] = useState();

    function onChangeEmail(e) {
        setEmail(e.target.value);
    }
    function onChangePassword(e) {
        setPassword(e.target.value);
    }
    function handleLogin(e){
        e.preventDefault();

        form.validateAll();

        if(checkBtn.context._errors.length === 0){
            login(email, password).then(
                () => {
                    Navigate("/successfullLogin")
                    window.location.reload();
                },
                error => {
                    alert("Logowanie nie powiodło się");
                }
            )
        }
    }

    return (
        <div className="col-md-12">
          <div className="card card-container">
            <img
                style={{"align-self": "center"}}
                src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
                alt="profile-img"
                className="profile-img-card"
                width="300"
                height="300"
            />
  
            <Form
              onSubmit={handleLogin}
              ref={c => {
                setForm(c);
              }}
            >
              <div className="form-group">
                <label htmlFor="email">Email</label>
                <Input
                  type="text"
                  className="form-control"
                  name="email"
                  value={email}
                  onChange={onChangeEmail}
                  validations={[required]}
                />
              </div>
  
              <div className="form-group">
                <label htmlFor="password">Password</label>
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
                <button
                  className="btn btn-primary btn-block"
                >
                  <span>Login</span>
                </button>
              </div>
              <CheckButton
                style={{ display: "none" }}
                ref={c => {
                    setCheckBtn(c)
                }}
              />
            </Form>
          </div>
        </div>
      );
}