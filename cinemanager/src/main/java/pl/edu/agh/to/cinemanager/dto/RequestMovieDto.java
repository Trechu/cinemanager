package pl.edu.agh.to.cinemanager.dto;

public record RequestMovieDto(String title, String description, String director,
                              String posterUrl, int length, long genreId) {
}
