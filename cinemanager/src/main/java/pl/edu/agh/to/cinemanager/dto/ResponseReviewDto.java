package pl.edu.agh.to.cinemanager.dto;

import java.math.BigDecimal;

public record ResponseReviewDto(long id, long movieId, ResponseReviewUserDto user, BigDecimal rating, String content) {
}
