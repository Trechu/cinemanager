package pl.edu.agh.to.cinemanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.cinemanager.dto.ResponseTicketDto;
import pl.edu.agh.to.cinemanager.model.Ticket;
import pl.edu.agh.to.cinemanager.service.TicketService;

import java.util.List;

@RestController
@RequestMapping(path = "api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public List<ResponseTicketDto> getTickets(Authentication authentication,
                           @RequestParam(value = "past", defaultValue = "false", required = false) boolean past) {
        if (past) {
            return ticketService.getAllTicketsForCustomer(authentication.getName());
        } else {
            return ticketService.getFutureTicketsForCustomer(authentication.getName());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseTicketDto getTicket(Authentication authentication,
                                       @PathVariable("id") Ticket ticket) {
        return ticketService.getTicketByIdForCustomer(authentication.getName(), ticket);
    }

    @PostMapping("/{id}/validate")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validateTicket(@PathVariable("id") Ticket ticket) {
        ticketService.validateTicket(ticket);
    }
}
