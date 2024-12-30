package pl.edu.agh.to.cinemanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.RequestOrderDto;
import pl.edu.agh.to.cinemanager.dto.ResponseOrderDto;
import pl.edu.agh.to.cinemanager.model.Order;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.model.UserRole;
import pl.edu.agh.to.cinemanager.service.AuthService;
import pl.edu.agh.to.cinemanager.service.OrderService;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping(path = "api/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;

    public OrderController(OrderService orderService, AuthService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseOrderDto getOrder(Authentication authentication, @PathVariable("id") Order order) {
        if (authService.hasRole(UserRole.MANAGER, authentication.getAuthorities())) {
            return orderService.orderToResponseDto(order, true);
        } else if (!Objects.equals(order.getUser().getEmail(), authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            return orderService.orderToResponseDto(order, false);
        }
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseOrderDto> createOrder(Authentication authentication, @RequestBody RequestOrderDto orderDto){
        ResponseOrderDto response = orderService.createOrderForUserByEmail(authentication.getName(), orderDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/orders/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{id}/payment")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePayment(@PathVariable("id") Order order) {
        orderService.updatePayment(order);
    }
}
