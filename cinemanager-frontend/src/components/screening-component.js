import { useEffect, useState } from "react"
import { fetch_screenings } from "../services/screening-service"

function Screening(key, posterUrl, title, genre, length, screeningType, startDate, includeDivider){

    const days = ["Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota", "Niedziela"]
    var date = new Date(startDate)
    var formattedDate = days[date.getDay()] + " " + (date.getDate() > 9 ? date.getDate() : "0" + date.getDate()) + "/" + (date.getMonth() + 1) + "/" + date.getFullYear() + " " + date.getHours() + ":" + (date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes())

    return (
        <div>
            <div className="screening-card" key={key}>
                <div className="screening-card-image-container">
                    <img src={posterUrl} alt={posterUrl} className="img-thumbnail"></img>
                </div>
                <div className="screening-card-details-container">
                    <h2>{title}</h2>
                    <h4>{genre} | {length} min</h4>
                    <h4>Rodzaj seansu: {screeningType} | {formattedDate}</h4>
                    <button type="button" className="btn btn-primary buy-btn">Kup bilet</button>
                </div>
            </div>
            {includeDivider && 
                (<hr className="divider-horizontal" />)
            }   
        </div>
    )
}

export default function Screenings(){

    const [screenings, setScreenings] = useState();

    const urlParams = new URLSearchParams(window.location.search);

    async function getScreenings(){
        const fetchedScreenings = (await fetch_screenings(urlParams.get('page'), urlParams.get('size'))).data
        setScreenings(fetchedScreenings)
    }
    
    useEffect(() => {
        getScreenings()
    }, [])

    function ChangePage(increase){
        urlParams.set('page', increase ? parseInt(urlParams.get('page')) + 1 : (urlParams.get('page') > 0 ? parseInt(urlParams.get('page')) - 1 : 0))
        window.history.replaceState(null, null, "?"+urlParams.toString())
        getScreenings()
    }

    return (
        <div>
            {screenings != undefined && 
                screenings.content.map((element, index) => Screening(
                        element.id,
                        element.movie.posterUrl,
                        element.movie.title,
                        element.movie.genre.name,
                        element.movie.length,
                        element.screeningType.name,
                        element.startDate,
                        index != screenings.content.length-1
                    ))
            }
            <div className="btn-holder">
                <button type="button" className="btn btn-primary page-btn" onClick={() => ChangePage(false)}>Poprzednia strona</button>
                <button type="button" className="btn btn-primary page-btn" onClick={() => ChangePage(true)}>Kolejna strona</button>
            </div>
        </div>
    )
}