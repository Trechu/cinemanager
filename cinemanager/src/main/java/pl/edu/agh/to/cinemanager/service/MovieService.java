package pl.edu.agh.to.cinemanager.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<ResponseMovieDto> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))))
                .stream().map(this::movieToReponseDto).toList();
    }
}
