import { useEffect, useState } from "react";
import { fetchHighestAttendanceScreenings, fetchHighestRatedMovies, fetchTicketsSold } from "../services/statistics-service";
import { Link } from "react-router-dom";

export default function Statistics() {
    const [highestRatedMovies, setHighestRatedMovies] = useState([]);
    const [highestAttendanceScreenings, setHighestAttendanceScreenings] = useState([]);

    const [after, setAfter] = useState(null);
    const [before, setBefore] = useState(null);
    const [ticketsSold, setTicketsSold] = useState([]);

    async function updateHighestRatedMovies() {
        setHighestRatedMovies(await fetchHighestRatedMovies())
    }

    async function updateHighestAttendanceScreenings() {
        setHighestAttendanceScreenings(await fetchHighestAttendanceScreenings())
    }

    async function updateTicketsSold(after, before) {
        setTicketsSold(await fetchTicketsSold(after, before))
    }

    useEffect(() => {
        updateHighestRatedMovies();
        updateHighestAttendanceScreenings();
    }, []);

    useEffect(() => {
        updateTicketsSold(after, before);
    }, [after, before]);

    return (
        <div className="container mt-5">
            <div className="row">
                <div className="col-md-4 mb-4">
                    <div className="card-stats card">
                        <div className="card-header">
                            <h5 className="card-title">Najwyżej oceniane filmy</h5>
                        </div>
                        <div className="card-body">
                            {highestRatedMovies ? 
                                <table className="table stats-table">
                                    <thead>
                                        <tr>
                                            <th scope="col">Tytuł</th>
                                            <th scope="col">Ocena</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {highestRatedMovies.map((movie, index) => (
                                            <tr key={index}>
                                                <td><Link className="table-link" to={`/movies/${movie.movie.id}`}>{movie.movie.title}</Link></td>
                                                <td>{movie.rating}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            : <></>}
                        </div>
                    </div>
                </div>
                <div className="col-md-8 mb-4">
                    <div className="card-stats card">
                        <div className="card-header">
                            <h5 className="card-title">Seanse z największym obłożeniem sali</h5>
                        </div>
                        <div className="card-body">
                            {highestAttendanceScreenings ? 
                                <table className="table stats-table">
                                    <thead>
                                        <tr>
                                            <th scope="col">Tytuł</th>
                                            <th scope="col">Data</th>
                                            <th scope="col">Rodzaj</th>
                                            <th scope="col">Obłożenie</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {highestAttendanceScreenings.map((screening, index) => (
                                            <tr key={index}>
                                                <td><Link className="table-link" to={`/movies/${screening.screening.movie.id}`}>{screening.screening.movie.title}</Link></td>
                                                <td>
                                                    <Link className="table-link" to={`/order?screening=${screening.screening.id}`}>
                                                    {
                                                        new Date(screening.screening.startDate).toLocaleDateString("pl-PL", {
                                                            weekday: "short",
                                                            year: "numeric",
                                                            month: "2-digit",
                                                            day: "2-digit",
                                                            hour: "2-digit",
                                                            minute: "2-digit",
                                                        })
                                                    }
                                                    </Link>
                                                </td>
                                                <td>{screening.screening.screeningType.name}</td>
                                                <td>{parseFloat(screening.attendancePercentage * 100).toFixed(1)} %</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            : <></>}
                        </div>
                    </div>
                </div>
                <div className="col-md-7 mb-4">
                    <div className="card-stats card">
                        <div className="card-header">
                            <h5 className="card-title">Najczęściej wybierane siedzenia</h5>
                        </div>
                        <div className="card-body">
                            <p className="card-text">todo</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-5 mb-4">
                    <div className="card-stats card">
                        <div className="card-header">
                            <h5 className="card-title">Liczba biletów na film</h5>
                        </div>
                        <div className="card-body">
                            <div className="row mb-4">
                                <div className="col-md-6">
                                    <label htmlFor="afterDate" className="form-label">Od</label>
                                    <input
                                        type="date"
                                        id="afterDate"
                                        className="form-control"
                                        value={after ? after.toISOString().split('T')[0] : ''}
                                        onChange={(e) => {
                                            const date = e.target.value ? new Date(Date.UTC(new Date(e.target.value).getUTCFullYear(), new Date(e.target.value).getUTCMonth(), new Date(e.target.value).getUTCDate(), 0, 0, 0)) : null;
                                            setAfter(date);
                                        }}
                                    />
                                </div>
                                <div className="col-md-6">
                                    <label htmlFor="beforeDate" className="form-label">Do</label>
                                    <input
                                        type="date"
                                        id="beforeDate"
                                        className="form-control"
                                        value={before ? before.toISOString().split('T')[0] : ''}
                                        onChange={(e) => {
                                            const date = e.target.value ? new Date(Date.UTC(new Date(e.target.value).getUTCFullYear(), new Date(e.target.value).getUTCMonth(), new Date(e.target.value).getUTCDate(), 23, 59, 59)) : null;
                                            setBefore(date);
                                        }}
                                    />
                                </div>
                            </div>
                            {highestRatedMovies ? 
                                <table className="table stats-table">
                                    <thead>
                                        <tr>
                                            <th scope="col">Tytuł</th>
                                            <th scope="col">Bilety</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {ticketsSold.map((stats, index) => (
                                            <tr key={index}>
                                                <td><Link className="table-link" to={`/movies/${stats.movie.id}`}>{stats.movie.title}</Link></td>
                                                <td>{stats.ticketsSold}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            : <></>}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}