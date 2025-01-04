package pl.edu.agh.to.cinemanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to.cinemanager.service.ReviewService;

@RestController
@RequestMapping(path = "api/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
}
