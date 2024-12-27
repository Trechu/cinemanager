import { admin_func } from "../services/admin-service"

export default function Admin(){
    return (
        <button onClick={admin_func}>Click me!</button>
    )
}