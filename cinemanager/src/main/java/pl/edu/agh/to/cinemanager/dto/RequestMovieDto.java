package pl.edu.agh.to.cinemanager.dto;

import pl.edu.agh.to.cinemanager.model.Genre;

public record RequestMovieDto(String title, String description, String director,
                              String posterUrl, int length, long genreId) {
}
