package pl.edu.agh.to.cinemanager.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.cinemanager.dto.RequestUserDto;
import pl.edu.agh.to.cinemanager.dto.ResponseUserDto;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private User getUserFromDto(RequestUserDto requestUserDto) {
        return new User(requestUserDto.firstName(), requestUserDto.lastName(),
                requestUserDto.email(), requestUserDto.password(), requestUserDto.role());
    }

    public ResponseUserDto getResponseUserDto(User user) {
        return new ResponseUserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }

    public User addUser(RequestUserDto requestUserDto) {
        User user = getUserFromDto(requestUserDto);
        return userRepository.save(user);
    }
}
