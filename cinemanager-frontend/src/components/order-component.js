import { useEffect, useState } from "react";
import { fetch_screening_info, fetch_screening_seats } from "../services/order-service";

function GenerateRows(screeningInfo) {
    const layout = []
    if(screeningInfo != undefined){
        for (var i = 0; i < screeningInfo.cinemaRoom.rows; i++){
            const row = []
            for (var j = 0; j < screeningInfo.cinemaRoom.seatsPerRow; j++){
                row.push(j)
            }
            layout.push(row)
        }
    }
    return layout
}

function Seat(row, seatNumber) {

    return (
        <div className={"seat"} >
            <span className="seat-identificator">
                {seatNumber}
            </span>
            <svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
                    <g id={"seat1;row"+row+";position"+seatNumber} fill="white" stroke="green" strokeWidth="5">
                        <rect id={"seat2;row"+row+";position"+seatNumber} width="80" height="50" x="10" y="10" rx="10" ry="10" fill="green" />
                        <rect id={"seat3;row"+row+";position"+seatNumber} width="60" height="20" x="20" y="70" rx="10" ry="10" fill="green" />
                    </g>
                </svg>
        </div>
    )
}

export default function Order() {

    const [screeningInfo, setScreeningInfo] = useState();

    const urlParams = new URLSearchParams(window.location.search);

    useEffect(() => {

        async function markSeats(takenSeats){
            for(var takenSeat of takenSeats){
                var seat1 = document.getElementById("seat1;row"+takenSeat.row+";position"+takenSeat.position)
                seat1.setAttribute("stroke", "grey") 
                var seat2 = document.getElementById("seat2;row"+takenSeat.row+";position"+takenSeat.position)
                seat2.setAttribute("fill", "grey")
                var seat3 = document.getElementById("seat3;row"+takenSeat.row+";position"+takenSeat.position)
                seat3.setAttribute("fill", "grey")  
            }
        }

        async function getSeats(screeningId) {
            const fetchedSeats = (await fetch_screening_seats(screeningId)).data
            
            markSeats(fetchedSeats)
        }

        async function getScreeningInfo(screeningId) {
            const fetchedInfo = (await fetch_screening_info(screeningId)).data
            setScreeningInfo(fetchedInfo)

            getSeats(screeningId)
        }

        getScreeningInfo(urlParams.get('screening'))
    }, [])

    return (
        <div className="order-details" id="details">
            <div className="screen-mock"><span style={{ "color": "black", "margin-left": "45%", "opacity": "33%", "fontWeight": "500" }}>EKRAN</span></div>
            {GenerateRows(screeningInfo).map((row, rowIndex) => {
                return (
                    <div className="row">
                        <span className="row-identificator">
                            {rowIndex}
                        </span>
                        {row.map((seatNumber, seatIndex) => {
                            return Seat(rowIndex, seatNumber)
                        })}
                    </div>
                )
            })}
        </div>
    )
}