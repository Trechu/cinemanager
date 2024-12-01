package pl.edu.agh.to.cinemanager.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.model.UserRole;
import pl.edu.agh.to.cinemanager.repository.UserRepository;

@Configuration
public class DataConfig {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User user1 = new User("Jan", "Kowalski", "jan@mail.com", passwordEncoder.encode("password"), UserRole.ADMINISTRATOR);
                User user2 = new User("Kamil", "Slimak", "kamil@snail.com", passwordEncoder.encode("password"), UserRole.CUSTOMER);
                userRepository.save(user1);
                userRepository.save(user2);
            }
        };
    }
}
