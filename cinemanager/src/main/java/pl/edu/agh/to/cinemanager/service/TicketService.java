package pl.edu.agh.to.cinemanager.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.cinemanager.repository.TicketRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
}
