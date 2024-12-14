package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "screenings")
public class Screening {

    @Id
    @GeneratedValue
    private long id;

    private LocalDate startDate;

    @ManyToOne
    private Movie movie;
    @ManyToOne
    private CinemaRoom cinemaRoom;

    public Screening() {}

    public Screening(LocalDate startDate, Movie movie, CinemaRoom cinemaRoom) {
        this.startDate = startDate;
        this.movie = movie;
        this.cinemaRoom = cinemaRoom;
    }

    public long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
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
}
