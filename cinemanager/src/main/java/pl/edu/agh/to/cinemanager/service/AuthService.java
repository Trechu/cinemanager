package pl.edu.agh.to.cinemanager.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestUserDto;
import pl.edu.agh.to.cinemanager.dto.ResponseUserDto;
import pl.edu.agh.to.cinemanager.model.SecurityUser;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.model.UserRole;
import pl.edu.agh.to.cinemanager.repository.UserRepository;

import java.util.Collection;

@Service
public class AuthService {
    private final RoleHierarchy roleHierarchy;
    private final UserService userService;

    public AuthService(RoleHierarchy roleHierarchy, UserService userService) {
        this.roleHierarchy = roleHierarchy;
        this.userService = userService;
    }

    public boolean hasRole(UserRole userRole, Collection<? extends GrantedAuthority> authorities) {
        return roleHierarchy.getReachableGrantedAuthorities(authorities).stream()
                .anyMatch(authority -> authority.getAuthority().equals(SecurityUser.ROLE_PREFIX + userRole));
    }

    public ResponseUserDto registerUser(RequestUserDto requestUserDto, Authentication authentication) {
        if (requestUserDto.role() != UserRole.CUSTOMER
                && (authentication == null || !hasRole(requestUserDto.role(), authentication.getAuthorities()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (userService.findUserByEmail(requestUserDto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return userService.getResponseUserDto(userService.addUser(requestUserDto));
    }
}
