package pl.edu.agh.to.cinemanager.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.RequestScreeningDto;
import pl.edu.agh.to.cinemanager.dto.ResponseScreeningDto;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.model.Screening;
import pl.edu.agh.to.cinemanager.service.ScreeningService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping("")
    public Page<ResponseScreeningDto> getAllScreenings(Pageable pageable, @RequestParam("movieId") Optional<Movie> movie) {
        if (movie.isPresent()) {
            return screeningService.getAllScreeningByMovie(movie.get(), pageable);
        } else {
            return screeningService.getAllScreenings(pageable);
        }
    }

    @GetMapping("{id}")
    public ResponseScreeningDto getScreeningById(@PathVariable("id") Screening screening) {
        return screeningService.screeningToScreeningDto(screening);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ResponseScreeningDto> createScreening(@RequestBody RequestScreeningDto screeningDto) {
        ResponseScreeningDto responseScreeningDto = screeningService.createScreening(screeningDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/movies/{id}")
                .buildAndExpand(responseScreeningDto.id()).toUri();

        return ResponseEntity.created(location).body(responseScreeningDto);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateScreening(@PathVariable("id") Screening screening, @RequestBody RequestScreeningDto screeningDto) {
        screeningService.updateScreening(screening, screeningDto);
    }
}
