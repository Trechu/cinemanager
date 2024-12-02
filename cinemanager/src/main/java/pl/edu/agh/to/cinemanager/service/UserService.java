package pl.edu.agh.to.cinemanager.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestUserDto;
import pl.edu.agh.to.cinemanager.dto.ResponseUserDto;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.model.UserRole;
import pl.edu.agh.to.cinemanager.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public List<ResponseUserDto> getUsers() {
        return userRepository.findAll().stream().map(UserService::getResponseUserDto).toList();
    }

    public ResponseUserDto getUser(User user, Authentication authentication) {
        if (!user.getEmail().equals(authentication.getName())
                && !authService.hasRole(UserRole.MANAGER, authentication.getAuthorities())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return getResponseUserDto(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private User getUserFromDto(RequestUserDto requestUserDto) {
        return new User(requestUserDto.firstName(), requestUserDto.lastName(),
                requestUserDto.email(), requestUserDto.password(), requestUserDto.role());
    }

    public static ResponseUserDto getResponseUserDto(User user) {
        return new ResponseUserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }

    public User addUser(RequestUserDto requestUserDto) {
        User user = getUserFromDto(requestUserDto);
        return userRepository.save(user);
    }

    public ResponseUserDto registerUser(RequestUserDto requestUserDto, Authentication authentication) {
        if (requestUserDto.role() != UserRole.CUSTOMER
                && (authentication == null || !authService.hasRole(requestUserDto.role(), authentication.getAuthorities()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (findUserByEmail(requestUserDto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return getResponseUserDto(addUser(requestUserDto));
    }

    public void deleteUser(User user, Authentication authentication) {
        if (!user.getEmail().equals(authentication.getName())
                || !authService.hasRole(user.getRole(), authentication.getAuthorities())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        userRepository.delete(user);
    }
}
