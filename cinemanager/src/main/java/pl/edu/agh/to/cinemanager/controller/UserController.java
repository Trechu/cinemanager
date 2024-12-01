package pl.edu.agh.to.cinemanager.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getUsers();
    }
}
