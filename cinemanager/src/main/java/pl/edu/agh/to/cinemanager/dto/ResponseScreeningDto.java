package pl.edu.agh.to.cinemanager.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public record ResponseScreeningDto(long id, LocalDateTime start_date, ResponseScreeningTypeDto screeningTypeDto,
                                   Optional<ResponseMovieDto> responseMovieDto, Optional<ResponseCinemaRoomDto> cinemaRoomDto) {
}
