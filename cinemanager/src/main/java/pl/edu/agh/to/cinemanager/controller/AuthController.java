package pl.edu.agh.to.cinemanager.controller;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.configuration.SecurityConfig;
import pl.edu.agh.to.cinemanager.dto.RequestUserDto;
import pl.edu.agh.to.cinemanager.dto.ResponseUserDto;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.service.AuthService;
import pl.edu.agh.to.cinemanager.service.TokenService;
import pl.edu.agh.to.cinemanager.service.UserService;

import java.net.URI;
import java.net.URL;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api")
public class AuthController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;
    private final UserService userService;

    public AuthController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        try {
            LOG.debug("Token requested for user: '{}'", authentication.getName());
            return tokenService.generateToken(authentication);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> register(Authentication authentication, @RequestBody RequestUserDto requestUserDto) {
        ResponseUserDto responseUserDto = userService.registerUser(requestUserDto, authentication);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/users/{id}")
                .buildAndExpand(responseUserDto.id()).toUri();

        return ResponseEntity.created(location).body(responseUserDto);
    }
}
