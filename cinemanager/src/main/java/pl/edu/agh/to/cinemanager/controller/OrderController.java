package pl.edu.agh.to.cinemanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.RequestOrderDto;
import pl.edu.agh.to.cinemanager.dto.ResponseOrderDto;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.service.OrderService;

import java.net.URI;

@RestController
@RequestMapping(path = "api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseOrderDto> createOrder(Authentication authentication, @RequestBody RequestOrderDto orderDto){
        ResponseOrderDto response = orderService.createOrderForUserByEmail(authentication.getName(), orderDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/orders/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }
}
