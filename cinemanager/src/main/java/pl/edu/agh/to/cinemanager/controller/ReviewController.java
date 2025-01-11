package pl.edu.agh.to.cinemanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.cinemanager.dto.ResponseReviewDto;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.model.Review;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.repository.specification.ReviewSpecification;
import pl.edu.agh.to.cinemanager.service.ReviewService;

@RestController
@RequestMapping(path = "api/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("")
    public Page<ResponseReviewDto> getAllReviews(Pageable pageable, Authentication authentication,
                                                 @RequestParam(value = "movieId", required = false) Movie movie,
                                                 @RequestParam(value = "userId", required = false) User user) {
        Specification<Review> reviewSpecification = ReviewSpecification.movie(movie)
                .and(ReviewSpecification.user(user));

        return reviewService.getAllReviews(reviewSpecification, pageable);
    }
}
