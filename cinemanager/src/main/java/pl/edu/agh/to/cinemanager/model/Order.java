package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private BigDecimal totalPrice;

    private boolean paid = false;

    private boolean cancelled = false;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order")
    private final Set<Ticket> ticketSet = new HashSet<>();

    public Order() {}

    public Order(LocalDateTime date, BigDecimal totalPrice, User user) {
        this.date = date;
        this.totalPrice = totalPrice;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Ticket> getTicketSet() {
        return ticketSet;
    }

    public void addTicket(Ticket ticket) {
        ticketSet.add(ticket);
    }
}
