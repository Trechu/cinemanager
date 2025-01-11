import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { fetch_movies, fetch_movie, fetch_rating, fetch_reviews, post_review, delete_review, update_review } from "../services/movie-service";
import { fetch_genries } from "../services/genre-service";
import { API_STATIC_URL } from "../services/api-url-config"
import Rating from "react-rating"
import { getCurrentUser } from "../services/authentication-service";

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
    const [rating, setRating] = useState(0.0);

    const user = getCurrentUser();
    const [currentUser, setCurrentUser] = useState(user ? JSON.parse(atob(user.split('.')[1])) : null);
    const hasAccess = (currentUser.scope == "ROLE_MANAGER" || currentUser.scope == "ROLE_ADMINISTRATOR");

    const [page, setPage] = useState(0);
    const [pageInfo, setPageInfo] = useState({ totalPages: 0, totalElements: 0 });
    const [reviews, setReviews] = useState([]);

    const [userReview, setUserReview] = useState("");
    const [userReviewRating, setUserReviewRating] = useState(0.0);

    async function getMovie() {
        const fetchedMovie = await fetch_movie(id);
        setMovie(fetchedMovie);
    }

    async function getMovieRating() {
        const fetchedRating = await fetch_rating(id);
        setRating(fetchedRating);
    }

    async function getMovieReviews() {
        const fetchedReviews = await fetch_reviews(id, page, 3);
        setReviews(fetchedReviews.content);
        setPageInfo(fetchedReviews.page);
    }
    
    useEffect(() => {
        getMovie();
        getMovieRating();
    }, [id]);

    useEffect(() => {
        getMovieReviews();
    }, [id, page]);

    if (!movie) {
        return <p>Film nie został znaleziony</p>;
    }

    async function handleSubmitReview() {
        if (userReview !== "") {
            post_review(id, userReview, userReviewRating)
            .then(res => {
                setUserReview("");
                setUserReviewRating(0);
                setPage(0);
                getMovieRating();
                getMovieReviews();
            })
            .catch(err => console.warn(err))
        }
    }

    async function handleDeleteReview(id) {
        delete_review(id)
        .then(res => {
            getMovieRating();
            getMovieReviews();
        })
        .catch(err => console.warn(err))
    }

    async function handleEditReview(reviewId, content, rating) {
        let newContent = window.prompt("Podaj nową treść recenzji:", content);
        let newRating = Number(window.prompt("Podaj nową ocenę (0.0 - 5.0):", rating));

        if (newContent !== null && newRating !== null) {
            update_review(reviewId, id, newContent, newRating)
            .then(res => {
                getMovieRating();
                getMovieReviews();
            })
            .catch(err => console.warn(err))
        }
    }

    const canGoBack = page > 0;
    const canGoForward = page + 1 < pageInfo.totalPages;

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

    const FilledStar = (props) => <svg xmlns="http://www.w3.org/2000/svg" width={props.size} height={props.size} fill="currentColor" class="bi bi-star-fill" viewBox="0 0 16 16"><path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/></svg>
    const EmptyStar = (props) => <svg xmlns="http://www.w3.org/2000/svg" width={props.size} height={props.size} fill="currentColor" class="bi bi-star" viewBox="0 0 16 16"><path d="M2.866 14.85c-.078.444.36.791.746.593l4.39-2.256 4.389 2.256c.386.198.824-.149.746-.592l-.83-4.73 3.522-3.356c.33-.314.16-.888-.282-.95l-4.898-.696L8.465.792a.513.513 0 0 0-.927 0L5.354 5.12l-4.898.696c-.441.062-.612.636-.283.95l3.523 3.356-.83 4.73zm4.905-2.767-3.686 1.894.694-3.957a.56.56 0 0 0-.163-.505L1.71 6.745l4.052-.576a.53.53 0 0 0 .393-.288L8 2.223l1.847 3.658a.53.53 0 0 0 .393.288l4.052.575-2.906 2.77a.56.56 0 0 0-.163.506l.694 3.957-3.686-1.894a.5.5 0 0 0-.461 0z"/></svg>

    return (
        <div>
            <div className="container">
                <div className="row">
                    <div className="col-md-4">
                        <img src={API_STATIC_URL + posterUrl} alt={title} className="img-thumbnail" />
                    </div>
                    <div className="col-md-8">
                        <h1>{title}</h1>
                        <div className="container row" style={{gap: 10, marginBottom: 10, alignItems: "center"}}>
                        <div>
                        <Rating
                            fullSymbol={<FilledStar size={25} />}
                            emptySymbol={<EmptyStar size={25} />}
                            fractions={2}
                            initialRating={rating}
                            readonly
                        />
                        </div>
                        
                            <div>
                                ({rating})
                            </div>
                        </div>
                        
                        <h4>{name} | {length} min</h4>
                        <h4>Reżyser: {firstName} {lastName}</h4>
                        <p>{description}</p>
                    </div>
                </div>
                <div className="row d-flex justify-content-center mb-5 mt-5">
                    <div className="col-md-8">
                        <div class="list-group mb-3" style={{gap: 10}}>
                            <div class="list-group-item" style={{backgroundColor: "#212529", color: "white"}}>
                            <textarea class="form-control mt-2 review-box" rows="3" placeholder="Wpisz swoją recenzję..." onChange={(e) => setUserReview(e.target.value)} value={userReview}></textarea>
                            <div class="d-flex justify-content-between mt-2">
                                <div className="d-flex align-items-center">
                                    <div style={{marginTop: -3}}>
                                        <Rating
                                            fullSymbol={<FilledStar size={20} />}
                                            emptySymbol={<EmptyStar size={20} />}
                                            fractions={2}
                                            initialRating={userReviewRating}
                                            onChange={setUserReviewRating}
                                        />
                                    </div>
                                </div>
                                <button class="btn btn-sm btn-primary" onClick={handleSubmitReview}>Opublikuj</button>
                            </div>
                        </div>
                            {
                                reviews.map(review => 
                                    
                                    <div class="list-group-item" style={{backgroundColor: "#212529", color: "white"}} key={review.id}>
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div className="d-flex align-items-center" style={{gap: 10}}>
                                               <div style={{marginTop: -3}}>
                                                    <Rating
                                                        fullSymbol={<FilledStar size={20} />}
                                                        emptySymbol={<EmptyStar size={20} />}
                                                        fractions={2}
                                                        initialRating={review.rating}
                                                        readonly
                                                    />
                                                </div>
                                                <h5 class="mb-0">{review.user.firstName}</h5>
                                            </div>
                                            <div className="d-flex align-items-center" style={{gap: 5, minHeight: 40}}>
                                                {currentUser.userId === review.user.id || hasAccess ? 
                                                    <>
                                                        <button class="btn btn-sm btn-warning me-2" onClick={() => handleEditReview(review.id, review.content, review.rating)}>Edytuj</button>
                                                        <button class="btn btn-sm btn-danger" onClick={() => handleDeleteReview(review.id)}>Usuń</button>
                                                    </>  
                                                : <></>}
                                            </div>
                                        </div>
                                        <p class="mt-2">{review.content}</p>

                                    </div>
                                
                                )
                            }
                        </div>
                        <div className="d-flex justify-content-center">
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