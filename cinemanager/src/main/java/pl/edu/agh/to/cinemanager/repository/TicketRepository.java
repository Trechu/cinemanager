package pl.edu.agh.to.cinemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.cinemanager.model.Order;
import pl.edu.agh.to.cinemanager.model.Screening;
import pl.edu.agh.to.cinemanager.model.Ticket;
import pl.edu.agh.to.cinemanager.model.User;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByScreening(Screening screening);

    Set<Ticket> findAllByOrder(Order order);

    Optional<Ticket> findByScreeningAndSeatRowAndSeatPosition(Screening screening, int row, int position);

    List<Ticket> findByOrderPaidTrueAndOrderCancelledFalseAndScreeningStartDateAfterAndScreeningStartDateBeforeAndUserAndUsedFalse(
            LocalDateTime now, LocalDateTime till, User user
    );

    List<Ticket> findByOrderPaidTrueAndOrderCancelledFalseAndUser(User user);

    Optional<Ticket> findTicketByIdAndUser(long ticketId, User user);
}
