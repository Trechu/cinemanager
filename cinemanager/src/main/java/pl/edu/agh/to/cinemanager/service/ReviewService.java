package pl.edu.agh.to.cinemanager.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.cinemanager.repository.ReviewRepository;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
}
