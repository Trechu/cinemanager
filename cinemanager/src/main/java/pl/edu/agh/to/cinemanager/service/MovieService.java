package pl.edu.agh.to.cinemanager.service;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestMovieDto;
import pl.edu.agh.to.cinemanager.dto.ResponseMovieDto;
import pl.edu.agh.to.cinemanager.model.Genre;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.repository.MovieRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreService genreService;

    public Page<ResponseMovieDto> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))))
                .map(this::movieToResponseDto);
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public ResponseMovieDto createMovie(RequestMovieDto movieDto) {
        Movie movie = new Movie(
                movieDto.title(),
                movieDto.description(),
                movieDto.director(),
                movieDto.posterUrl(),
                movieDto.length(),
                getGenreFromRequestDto(movieDto)
        );

        save(movie);

        return movieToResponseDto(movie);
    }

    public void updateMovie(Movie movie, RequestMovieDto updatedMovieDto) {
        movie.setTitle(updatedMovieDto.title());
        movie.setDescription(updatedMovieDto.description());
        movie.setDirector(updatedMovieDto.director());
        movie.setPosterUrl(updatedMovieDto.posterUrl());
        movie.setLength(updatedMovieDto.length());
        movie.setGenre(getGenreFromRequestDto(updatedMovieDto));

        save(movie);
    }

    public ResponseMovieDto movieToResponseDto(Movie movie) {
        return new ResponseMovieDto(movie.getId(), movie.getTitle(), movie.getDescription(), movie.getDirector(),
                movie.getPosterUrl(), movie.getLength(), genreService.genreToResponseDto(movie.getGenre()));
    }

    private Genre getGenreFromRequestDto(RequestMovieDto movieDto) {
        return genreService.getGenreById(movieDto.genreId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This genre does not exist"));
    }

    private void save(Movie movie) {
        try {
            movieRepository.save(movie);
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
