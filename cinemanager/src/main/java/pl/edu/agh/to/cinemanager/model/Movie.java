package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Column(length = 128)
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    @Column(length = 256)
    private String director;
    @NotBlank
    @Column(length = 256)
    private String posterUrl;
    @NotNull
    private int length;

    @NotNull
    @ManyToOne
    private Genre genre;

    @OneToMany(mappedBy = "movie")
    private final Set<Review> reviewSet = new HashSet<>();

    public Movie() {}

    public Movie(String title, String description, String director, String posterUrl, int length, Genre genre) {
        this.title = title;
        this.description = description;
        this.director = director;
        this.posterUrl = posterUrl;
        this.length = length;
        this.genre = genre;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String text) {
        this.description = text;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Set<Review> getReviewSet() {
        return reviewSet;
    }

    public void addReview(Review review) {
        reviewSet.add(review);
    }
}
