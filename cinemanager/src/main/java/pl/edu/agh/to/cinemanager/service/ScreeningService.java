package pl.edu.agh.to.cinemanager.service;

import jakarta.validation.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestScreeningDto;
import pl.edu.agh.to.cinemanager.dto.ResponseScreeningDto;
import pl.edu.agh.to.cinemanager.model.CinemaRoom;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.model.Screening;
import pl.edu.agh.to.cinemanager.model.ScreeningType;
import pl.edu.agh.to.cinemanager.repository.ScreeningRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final ScreeningTypeService screeningTypeService;
    private final MovieService movieService;
    private final CinemaRoomService cinemaRoomService;

    public ScreeningService(ScreeningRepository screeningRepository, ScreeningTypeService screeningTypeService,
                            MovieService movieService, CinemaRoomService cinemaRoomService) {
        this.screeningRepository = screeningRepository;
        this.screeningTypeService = screeningTypeService;
        this.movieService = movieService;
        this.cinemaRoomService = cinemaRoomService;
    }

    public Page<ResponseScreeningDto> getAllScreenings(Pageable pageable) {
        return screeningRepository.findAll(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id")))
                )
                .map(this::screeningToScreeningDto);
    }

    public Page<ResponseScreeningDto> getAllScreeningByMovie(Movie movie, Pageable pageable) {
        return screeningRepository
                .findAllByMovie(movie,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id")))
                )
                .map(this::screeningToScreeningDto);
    }


    public ResponseScreeningDto screeningToScreeningDto(Screening screening) {
        return new ResponseScreeningDto(screening.getId(), screening.getStartDate(),
                screeningTypeService.screeningTypeToScreeningTypeDto(screening.getScreeningType()),
                movieService.movieToResponseDto(screening.getMovie()),
                cinemaRoomService.cinemaRoomToCinemaRoomResponseDto(screening.getCinemaRoom()));
    }

    public ResponseScreeningDto createScreening(RequestScreeningDto screeningDto) {
        Screening screening = new Screening(screeningDto.startDate(),
                getMovieFromRequestDto(screeningDto),
                getCinemaRoomFromRequestDto(screeningDto),
                getScreeningTypeFromRequestDto(screeningDto));

        save(screening);

        return screeningToScreeningDto(screening);
    }

    public void updateScreening(Screening screening, RequestScreeningDto screeningDto) {
        screening.setScreeningType(getScreeningTypeFromRequestDto(screeningDto));
        screening.setMovie(getMovieFromRequestDto(screeningDto));
        screening.setCinemaRoom(getCinemaRoomFromRequestDto(screeningDto));
        screening.setStartDate(screeningDto.startDate());

        save(screening);
    }

    private Movie getMovieFromRequestDto(RequestScreeningDto screeningDto) {
        return movieService.getMovieById(screeningDto.movieId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This movie does not exist"));
    }

    private CinemaRoom getCinemaRoomFromRequestDto(RequestScreeningDto screeningDto) {
        return cinemaRoomService.getCinemaRoomById(screeningDto.roomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This room does not exist"));
    }

    private ScreeningType getScreeningTypeFromRequestDto(RequestScreeningDto screeningDto) {
        return screeningTypeService.getScreeningTypeById(screeningDto.screeningTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This screening type does not exist"));
    }

    private void save(Screening screening) {
        try {
            screeningRepository.save(screening);
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
