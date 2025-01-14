import { AddCinemaRoom, AddScreeningType, AddMovie, AddDirector, AddGenre, AddScreening } from "./admin-add-component";

export default function Admin(){
    return (
        <div className="container">
            <AddScreening />
            <AddMovie />
            <AddGenre />
            <AddDirector />
            <AddScreeningType />
            <AddCinemaRoom />
        </div>
    )
}