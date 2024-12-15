package pl.edu.agh.to.cinemanager.dto;

public record ResponseMovieDto(long id, String title, String description, String director,
                               String posterUrl, int length, ResponseGenreDto genre) {
}
