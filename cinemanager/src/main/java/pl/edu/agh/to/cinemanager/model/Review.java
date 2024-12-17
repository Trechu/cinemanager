package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    @NotNull
    private String content;

    @ManyToOne
    private User user;

    @ManyToOne
    private Movie movie;

    public Review() {
    }

    public Review(BigDecimal rating, String content, User user, Movie movie) {
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.movie = movie;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
