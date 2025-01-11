package pl.edu.agh.to.cinemanager.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.MovieRatingDto;
import pl.edu.agh.to.cinemanager.dto.RequestMovieDto;
import pl.edu.agh.to.cinemanager.dto.ResponseMovieDto;
import pl.edu.agh.to.cinemanager.dto.ResponseMovieRatingDto;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.service.MovieService;
import pl.edu.agh.to.cinemanager.service.ReviewService;

@RestController
@RequestMapping(path = "api/movies")
@AllArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ReviewService reviewService;

    @GetMapping("")
    public Page<ResponseMovieDto> getAllMovies(Pageable pageable,
                                               @RequestParam(value = "genre", required = false) String genre) {
        if (genre != null && !genre.isEmpty()) {
            return movieService.getAllMoviesByGenre(genre, pageable);
        }
        return movieService.getAllMovies(pageable);
    }

    @GetMapping("/{id}")
    public ResponseMovieDto getMovieById(@PathVariable("id") Movie movie) {
        return movieService.movieToResponseDto(movie);
    }

    @GetMapping("/{id}/rating")
    public BigDecimal getMovieRating(@PathVariable("id") Movie movie) {
        return reviewService.getRating(movie);
    }

    @GetMapping("/highest-rated")
    public ResponseMovieRatingDto getHighestRatedMovies(){
        return reviewService.getHighestRatedMovies();
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ResponseMovieDto> createMovie(@RequestBody RequestMovieDto movieDto) {
        ResponseMovieDto responseMovieDto = movieService.createMovie(movieDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/movies/{id}")
                .buildAndExpand(responseMovieDto.id()).toUri();

        return ResponseEntity.created(location).body(responseMovieDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMovie(@PathVariable("id") Movie movie, @RequestBody RequestMovieDto updatedMovieDto) {
        movieService.updateMovie(movie, updatedMovieDto);
    }
}
