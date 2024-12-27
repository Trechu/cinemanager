package pl.edu.agh.to.cinemanager.dto;

import pl.edu.agh.to.cinemanager.model.Ticket;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record ResponseOrderDto(long id, LocalDateTime date, BigDecimal totalPrice, boolean paid, boolean cancelled, Set<Ticket> tickets) {
}
