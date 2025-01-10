import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { fetch_movies, fetch_movie } from "../services/movie-service";
import { fetch_genries } from "../services/genre-service";
import { API_STATIC_URL } from "../services/api-url-config"

function MovieCard({ movie }) {
    const navigate = useNavigate();

    const {
        id,
        title,
        posterUrl,
    } = movie;

    return (
        <div className="card m-2" onClick={() => navigate(`/movies/${id}`)}>
            <img src={API_STATIC_URL + posterUrl} className="card-img-top" alt={title} />
            <div className="card-overlay">{title}</div>
        </div>
    )
}

function Movie() {
    const { id } = useParams();
    const [movie, setMovie] = useState(null);

    async function getMovie() {
        const fetchedMovie = await fetch_movie(id);
        setMovie(fetchedMovie);
    }
    
    useEffect(() => {
        getMovie();
    }, [id]);

    if (!movie) {
        return <p>Film nie został znaleziony</p>;
    }

    const {
        title,
        genre: {
            name
        },
        length,
        director: {
            firstName,
            lastName
        },
        posterUrl,
        description,
    } = movie;

    return (
        <div>
            <div className="container">
                <div className="row">
                    <div className="col-md-4">
                        <img src={API_STATIC_URL + posterUrl} alt={title} className="img-thumbnail" />
                    </div>
                    <div className="col-md-8">
                        <h1>{title}</h1>
                        <h4>{name} | {length} min</h4>
                        <h4>Reżyser: {firstName} {lastName}</h4>
                        <p>{description}</p>
                    </div>
                </div>
            </div>
        </div>
    )
}

function Movies() {
    const [movies, setMovies] = useState([]);
    const [page, setPage] = useState(0);
    const [pageInfo, setPageInfo] = useState({ totalPages: 0, totalElements: 0 });
    const [selectedGenre, setSelectedGenre] = useState("");
    const [genres, setGenres] = useState([]);

    async function getMovies() {
        const fetchedMovies = await fetch_movies(page, 16, selectedGenre);
        setMovies(fetchedMovies.content);
        setPageInfo(fetchedMovies.page);
    }

    async function getGenres() {
        const fetchedGenres = await fetch_genries();
        setGenres(fetchedGenres);
        console.log(fetchedGenres);
    }

    useEffect(() => {
        getGenres();
    }, []);

    useEffect(() => {
        getMovies();
    }, [page, selectedGenre]);

    const canGoBack = page > 0;
    const canGoForward = page + 1 < pageInfo.totalPages;

    return (
        <div className="container">
            <div className="mb-3">
                <select
                    className="form-select"
                    value={selectedGenre}
                    onChange={(e) => {
                        setSelectedGenre(e.target.value);
                        setPage(0);
                    }}
                >
                    <option value="">Wszystkie gatunki</option>
                    {genres.map((genre) => (
                        <option key={genre.id} value={genre.name}>
                            {genre.name}
                        </option>
                    ))}
                </select>
            </div>
            <div className="container">
                <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3">
                    {movies.map((movie) => (
                        <div className="col" key={movie.id}>
                            <MovieCard movie={movie} />
                        </div>
                    ))}
                </div>
            </div>
            <div className="btn-holder">
                {canGoBack && (
                    <button
                        type="button"
                        className="btn btn-secondary mr-1"
                        onClick={() => setPage((prev) => Math.max(0, prev - 1))}
                    >
                        Poprzednia strona
                    </button>
                )}
                {canGoForward && (
                    <button
                        type="button"
                        className="btn btn-secondary mr-1"
                        onClick={() => setPage((prev) => prev + 1)}
                    >
                        Kolejna strona
                    </button>
                )}
            </div>
        </div>
    )
}

export {
    Movies,
    Movie
}