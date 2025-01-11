package pl.edu.agh.to.cinemanager.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.cinemanager.dto.ResponseReviewDto;
import pl.edu.agh.to.cinemanager.dto.ResponseReviewUserDto;
import pl.edu.agh.to.cinemanager.model.Review;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.repository.ReviewRepository;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Page<ResponseReviewDto> getAllReviews(Specification<Review> specification, Pageable pageable) {
        return reviewRepository
                .findAll(specification, PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    pageable.getSortOr(Sort.by(Sort.Direction.DESC, "id"))))
                .map(this::reviewToResponseDto);
    }

    public ResponseReviewDto reviewToResponseDto(Review review) {
        return new ResponseReviewDto(
                review.getId(),
                review.getMovie().getId(),
                userToResponseReviewDto(review.getUser()),
                review.getRating(),
                review.getContent()
        );
    }

    private ResponseReviewUserDto userToResponseReviewDto(User user) {
        return new ResponseReviewUserDto(user.getId(), user.getFirstName());
    }
}
