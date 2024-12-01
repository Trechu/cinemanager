package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue
    private long id;

    private int seatRow;
    private int seatPosition;
    private boolean valid = true;

    @ManyToOne
    private Screening screening;
    @ManyToOne
    private User user;

    public Ticket() {}

    public Ticket(int seatRow, int seatPosition) {
        this.seatRow = seatRow;
        this.seatPosition = seatPosition;
    }

    public long getId() {
        return id;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatPosition() {
        return seatPosition;
    }

    public void setSeatPosition(int seatPosition) {
        this.seatPosition = seatPosition;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
