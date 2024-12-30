package pl.edu.agh.to.cinemanager.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestOrderDto;
import pl.edu.agh.to.cinemanager.dto.ResponseOrderDto;
import pl.edu.agh.to.cinemanager.model.Order;
import pl.edu.agh.to.cinemanager.model.Screening;
import pl.edu.agh.to.cinemanager.model.ScreeningType;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.repository.OrderRepository;
import pl.edu.agh.to.cinemanager.repository.TicketRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final TicketService ticketService;
    private final ScreeningService screeningService;
    private final UserService userService;
    private final TicketRepository ticketRepository;

    public OrderService(OrderRepository orderRepository, TicketService ticketService, ScreeningService screeningService,
                        UserService userService, TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.ticketService = ticketService;
        this.screeningService = screeningService;
        this.userService = userService;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public ResponseOrderDto createOrderForUserByEmail(String email, RequestOrderDto orderDto){
        Screening screening = screeningService.getScreeningById(orderDto.screeningId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This screening does not exist"));
        ScreeningType screeningType = screening.getScreeningType();
        BigDecimal price = orderDto.types().stream().map(type -> {
            if (Objects.equals(type, "Ulgowy")) {
                return screeningType.getDiscountPrice();
            } else {
                return screeningType.getBasePrice();
            }
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
        User user = userService.findUserByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist"));
        Order order = new Order(LocalDateTime.now(), price, user);
        ticketService.createTicketsFromOrderInformation(orderDto.rows(), orderDto.seatNumbers(), orderDto.types(),
                order, screening, user);
        orderRepository.save(order);

        return orderToResponseDto(order);
    }

    public ResponseOrderDto orderToResponseDto(Order order){
        return new ResponseOrderDto(order.getId(), order.getDate(), order.getTotalPrice(),
                order.isPaid(), order.isCancelled(), ticketRepository.findAllByOrder(order));
    }
}
