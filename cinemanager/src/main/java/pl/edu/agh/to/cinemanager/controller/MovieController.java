package pl.edu.agh.to.cinemanager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to.cinemanager.service.MovieService;

@RestController
@RequestMapping(path = "movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
}
