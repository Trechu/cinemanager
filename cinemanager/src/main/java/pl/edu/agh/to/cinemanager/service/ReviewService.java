package pl.edu.agh.to.cinemanager.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.cinemanager.repository.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
}
