package pl.edu.agh.to.cinemanager.controller;

import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.cinemanager.dto.ResponseGenreDto;
import pl.edu.agh.to.cinemanager.model.Genre;
import pl.edu.agh.to.cinemanager.service.GenreService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("")
    public List<ResponseGenreDto> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public ResponseGenreDto getGenreById(@PathVariable("id") Genre genre) {
        return genreService.genreToResponseDto(genre);
    }
}
