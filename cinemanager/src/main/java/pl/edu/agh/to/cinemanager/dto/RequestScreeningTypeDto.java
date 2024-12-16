package pl.edu.agh.to.cinemanager.dto;

import java.math.BigDecimal;

public record RequestScreeningTypeDto(long id, String name, BigDecimal base_price, BigDecimal discount_price) {
}
