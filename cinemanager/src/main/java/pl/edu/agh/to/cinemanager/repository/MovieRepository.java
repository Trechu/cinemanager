package pl.edu.agh.to.cinemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.cinemanager.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
