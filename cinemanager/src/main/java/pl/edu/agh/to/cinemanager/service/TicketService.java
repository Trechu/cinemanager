package pl.edu.agh.to.cinemanager.service;

import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.ResponseTicketDto;
import pl.edu.agh.to.cinemanager.model.Order;
import pl.edu.agh.to.cinemanager.model.Screening;
import pl.edu.agh.to.cinemanager.model.Ticket;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.repository.TicketRepository;
import pl.edu.agh.to.cinemanager.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ScreeningService screeningService;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, ScreeningService screeningService, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.screeningService = screeningService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createTicketsFromOrderInformation(List<Integer> rows, List<Integer> seatNumbers, List<String> types, Order order, Screening screening, User user){
        Iterator<Integer> rowIterator = rows.iterator();
        Iterator<Integer> seatNumberIterator = seatNumbers.iterator();
        Iterator<String> typeIterator = types.iterator();
        List<Ticket> tickets = new ArrayList<>();
        while (rowIterator.hasNext()&&seatNumberIterator.hasNext()&&typeIterator.hasNext()){
            int row = rowIterator.next();
            int seatNumber = seatNumberIterator.next();
            if (ticketRepository.findByScreeningAndSeatRowAndSeatPositionAndOrderCancelledFalse(screening,row,seatNumber).isPresent()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One of the tickets is already taken");
            }

            boolean discounted = typeIterator.next().equals("Ulgowy");
            Ticket ticket = new Ticket(row, seatNumber, screening, user, order, discounted);

            tickets.add(ticket);
        }
        ticketRepository.saveAll(tickets);
    }

    public Page<ResponseTicketDto> getFutureTicketsForCustomer(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime till = now.plusHours(2);

        return ticketRepository.findByOrderPaidTrueAndOrderCancelledFalseAndScreeningStartDateAfterAndScreeningStartDateBeforeAndUserAndUsedFalse(now, till, user, pageable)
                .map(this::ticketToResponseDto);
    }

    public Page<ResponseTicketDto> getAllTicketsForCustomer(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist"));

        return ticketRepository.findByOrderPaidTrueAndOrderCancelledFalseAndUser(user, pageable)
                .map(this::ticketToResponseDto);
    }

    public ResponseTicketDto getTicketByIdForCustomer(String email, Ticket ticket) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exit"));

        return ticketRepository.findTicketByIdAndUser(ticket.getId(), user)
                .map(this::ticketToResponseDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket with id " + ticket.getId() + " does not exist or is not yours"));
    }

    @Transactional
    public void validateTicket(Ticket ticket) {
        if (ticket.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket is already used");
        } else {
            ticket.setUsed(true);
            ticketRepository.saveAll(List.of(ticket));
        }
    }

    public ResponseTicketDto ticketToResponseDto(Ticket ticket) {
        return new ResponseTicketDto(ticket.getId(), ticket.getSeatRow(), ticket.getSeatPosition(), ticket.isUsed(),
                screeningService.screeningToScreeningDto(ticket.getScreening()), ticket.isDiscounted());
    }
}
