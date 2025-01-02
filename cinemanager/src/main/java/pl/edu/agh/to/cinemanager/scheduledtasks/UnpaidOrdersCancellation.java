package pl.edu.agh.to.cinemanager.scheduledtasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.cinemanager.model.Order;
import pl.edu.agh.to.cinemanager.repository.OrderRepository;

import java.time.LocalDateTime;

@Component
public class UnpaidOrdersCancellation {

    private static final int PAYMENT_TIME_MINUTES = 15;
    private static final Logger logger = LoggerFactory.getLogger(UnpaidOrdersCancellation.class);
    private final OrderRepository orderRepository;

    public UnpaidOrdersCancellation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedRate = PAYMENT_TIME_MINUTES*60*1000)
    public void findAndCancelUnpaidOrders() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(PAYMENT_TIME_MINUTES);

        for (Order order : orderRepository.findAllByPaidFalseAndCancelledFalseAndDateBefore(fifteenMinutesAgo)) {
            order.setCancelled(true);
            orderRepository.save(order);

            logger.info("Order {} has been canceled because it has not been paid on time.", order.getId());
        }
    }
}
