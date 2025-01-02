package pl.edu.agh.to.cinemanager.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestOrderDto;
import pl.edu.agh.to.cinemanager.dto.ResponseOrderDto;
import pl.edu.agh.to.cinemanager.dto.ResponseTicketDto;
import pl.edu.agh.to.cinemanager.model.*;
import pl.edu.agh.to.cinemanager.repository.OrderRepository;
import pl.edu.agh.to.cinemanager.repository.TicketRepository;
import pl.edu.agh.to.cinemanager.repository.UserRepository;
import pl.edu.agh.to.cinemanager.repository.specification.OrderSpecification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final TicketService ticketService;
    private final ScreeningService screeningService;
    private final UserService userService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, TicketService ticketService, ScreeningService screeningService,
                        UserService userService, TicketRepository ticketRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.ticketService = ticketService;
        this.screeningService = screeningService;
        this.userService = userService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseOrderDto createOrderForUserByEmail(String email, RequestOrderDto orderDto){
        Screening screening = screeningService.getScreeningById(orderDto.screeningId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This screening does not exist"));
        ScreeningType screeningType = screening.getScreeningType();
        BigDecimal price = orderDto.tickets().stream().map(ticket -> {
            if (ticket.ticketType().equals(TicketType.DISCOUNTED)) {
                return screeningType.getDiscountPrice();
            } else {
                return screeningType.getBasePrice();
            }
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
        User user = userService.findUserByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist"));
        Order order = new Order(LocalDateTime.now(), price, user);

        orderRepository.save(order);

        ticketService.createTicketsFromOrderInformation(orderDto.tickets(),
                order, screening, user);

        return orderToResponseDto(order, true);
    }

    public ResponseOrderDto orderToResponseDto(Order order, boolean includeUserId){
        Optional<Long> userId = includeUserId ? Optional.of(order.getUser().getId()) : Optional.empty();

        List<ResponseTicketDto> tickets = ticketRepository.findAllByOrder(order).stream()
                .map(ticketService::ticketToResponseDto).toList();

        return new ResponseOrderDto(order.getId(), order.getDate(), order.getTotalPrice(),
                order.isPaid(), order.isCancelled(), tickets, userId);
    }

    public void updatePayment(Order order) {
        if (order.isPaid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is already paid");
        } else {
            order.setPaid(true);
            orderRepository.save(order);
        }
    }

    public Page<ResponseOrderDto> getAllOrders(Specification<Order> specification, Pageable pageable) {
        return orderRepository
                .findAll(specification, PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))))
                .map(order -> orderToResponseDto(order, true));
    }

    public Page<ResponseOrderDto> getOrdersForCustomer(Specification<Order> specification, Pageable pageable, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist"));

        Specification<Order> specificationWithUser = specification.and(OrderSpecification.user(user));

        return orderRepository
                .findAll(specificationWithUser, PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))))
                .map(order -> orderToResponseDto(order, false));
    }
}
