package pl.edu.agh.to.cinemanager.dto;

import java.util.List;

public record RequestOrderDto(long screeningId, List<Integer> rows, List<Integer> seatNumbers, List<String> types) {
}
