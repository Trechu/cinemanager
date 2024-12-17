package pl.edu.agh.to.cinemanager.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public record ResponseScreeningDto(long id, LocalDateTime startDate, ResponseScreeningTypeDto screeningType,
                                   Optional<ResponseMovieDto> movie, Optional<ResponseCinemaRoomDto> cinemaRoom) {
}
