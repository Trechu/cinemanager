package pl.edu.agh.to.cinemanager.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.model.Order;
import pl.edu.agh.to.cinemanager.model.Screening;
import pl.edu.agh.to.cinemanager.model.Ticket;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.repository.TicketRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
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
            if (ticketRepository.findByScreeningAndSeatRowAndSeatPosition(screening,row,seatNumber).isPresent()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One of the tickets is already taken");
            }

            boolean discounted = typeIterator.next().equals("Ulgowy");
            Ticket ticket = new Ticket(row, seatNumber, screening, user, order, discounted);

            tickets.add(ticket);
        }
        ticketRepository.saveAll(tickets);
    }
}
