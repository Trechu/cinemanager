package pl.edu.agh.to.cinemanager.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.cinemanager.dto.ResponseGenreDto;
import pl.edu.agh.to.cinemanager.model.Genre;
import pl.edu.agh.to.cinemanager.repository.GenreRepository;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public ResponseGenreDto genreToResponseDto(Genre genre) {
        return new ResponseGenreDto(genre.getId(), genre.getName());
    }

    public List<ResponseGenreDto> getAllGenres() {
        return genreRepository.findAll().stream().map(this::genreToResponseDto).toList();
    }
}
