package pl.edu.agh.to.cinemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.cinemanager.model.Screening;
import pl.edu.agh.to.cinemanager.model.Ticket;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByScreening(Screening screening);

    Optional<Ticket> findByScreeningAndSeatRowAndSeatPosition(Screening screening, int row, int position);
}
