package pl.edu.agh.to.cinemanager.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.cinemanager.repository.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
}
