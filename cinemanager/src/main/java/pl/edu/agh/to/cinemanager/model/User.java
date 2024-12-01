package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(length = 64)
    private String firstName;
    @Column(length = 64)
    private String lastName;
    @Column(length = 128, unique = true)
    private String email;
    @Column(length = 256)
    private String password;
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviewSet = new HashSet<>();
    @OneToMany(mappedBy = "user")
    private Set<Ticket> ticketSet = new HashSet<>();

    public User() {}

    public User(String firstName, String lastName, String email, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Set<Review> getReviewSet() {
        return reviewSet;
    }

    public Set<Ticket> getTicketSet() {
        return ticketSet;
    }
}
