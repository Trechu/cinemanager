package pl.edu.agh.to.cinemanager.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.cinemanager.repository.GenreRepository;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }
}
