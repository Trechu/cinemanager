package pl.edu.agh.to.cinemanager.dto;

import java.time.LocalDateTime;

public record RequestScreeningDto(long movieId, long roomId, LocalDateTime startDate, long screeningTypeId) {
}
