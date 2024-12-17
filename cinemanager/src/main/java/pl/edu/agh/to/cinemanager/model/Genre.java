package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Column(length = 64, unique = true)
    private String name;

    @OneToMany(mappedBy = "genre")
    private final Set<Movie> movieSet = new HashSet<>();

    public Genre() {}

    public Genre(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Movie> getMovieSet() {
        return movieSet;
    }

    public void addMovie(Movie movie) {
        movieSet.add(movie);
    }
}
