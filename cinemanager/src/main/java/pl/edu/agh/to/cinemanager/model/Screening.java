package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "screenings")
public class Screening {

    @Id
    @GeneratedValue
    private long id;

    private LocalDateTime startDate;

    @ManyToOne
    private Movie movie;
    @ManyToOne
    private CinemaRoom cinemaRoom;
    @ManyToOne
    private ScreeningType screeningType;

    public Screening() {}

    public Screening(LocalDateTime startDate, Movie movie, CinemaRoom cinemaRoom, ScreeningType screeningType) {
        this.startDate = startDate;
        this.movie = movie;
        this.cinemaRoom = cinemaRoom;
        this.screeningType = screeningType;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    public void setCinemaRoom(CinemaRoom cinemaRoom) {
        this.cinemaRoom = cinemaRoom;
    }

    public ScreeningType getScreeningType() {
        return screeningType;
    }

    public void setScreeningType(ScreeningType screeningType) {
        this.screeningType = screeningType;
    }
}
