import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import isEmail from "validator/lib/isEmail";

const required = value => {
    if(!value){
        alert("To pole jest wymagane");
    }
}

const email = value => {
    if(!isEmail(value)){
        alert("Podany email jest nieprawidłowy");
    }
}

export default function Validation() {
    return (
    <Form
        onSubmit={handleLogin}
         ref={c => {form = c;}}
    >
    <Input
        type="text"
        className="form-control"
        validations={[required, email]}
    />
    <CheckButton
        style={{ display: "none" }}
        ref={c => {checkBtn = c;}}
    />
    </Form>
    )
}