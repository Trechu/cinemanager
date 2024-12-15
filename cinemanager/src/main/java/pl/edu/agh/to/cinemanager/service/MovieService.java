package pl.edu.agh.to.cinemanager.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.cinemanager.dto.ResponseMovieDto;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.repository.MovieRepository;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreService genreService;

    public MovieService(MovieRepository movieRepository, GenreService genreService) {
        this.movieRepository = movieRepository;
        this.genreService = genreService;
    }

    public ResponseMovieDto movieToReponseDto(Movie movie) {
        return new ResponseMovieDto(movie.getId(), movie.getTitle(), movie.getDescription(), movie.getDirector(),
                movie.getPosterUrl(), movie.getLength(), genreService.genreToResponseDto(movie.getGenre()));
    }

    public List<ResponseMovieDto> getAllMovies() {
        return movieRepository.findAll().stream().map(this::movieToReponseDto).toList();
    }
}
