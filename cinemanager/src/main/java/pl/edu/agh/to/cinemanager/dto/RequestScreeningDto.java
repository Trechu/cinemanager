package pl.edu.agh.to.cinemanager.dto;

import java.time.LocalDateTime;

public record RequestScreeningDto(long movie_id, long room_id, LocalDateTime start_date, long screening_type_id) {
}
