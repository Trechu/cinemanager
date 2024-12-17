package pl.edu.agh.to.cinemanager.service;

import jakarta.validation.ConstraintViolationException;
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

    public List<ResponseScreeningDto> getAllScreenings(){
        return screeningRepository.findAll().stream().map(screening -> screeningToScreeningDto(screening, true, true)).toList();
    }

    public List<ResponseScreeningDto> getAllScreeningByMovie(Movie movie){
        return screeningRepository
                .findAllByMovie(movie)
                .stream()
                .map(screening -> screeningToScreeningDto(screening, false, false)).toList();
    }


    public ResponseScreeningDto screeningToScreeningDto(Screening screening, Boolean includeMovie, Boolean includeRoom){
        return new ResponseScreeningDto(screening.getId(), screening.getStartDate(),
                screeningTypeService.screeningTypeToScreeningTypeDto(screening.getScreeningType()),
                Optional.ofNullable(includeMovie ? movieService.movieToResponseDto(screening.getMovie()) : null),
                Optional.ofNullable(includeRoom ? cinemaRoomService.cinemaRoomToCinemaRoomResponseDto(screening.getCinemaRoom()) : null));
    }

    public ResponseScreeningDto createScreening(RequestScreeningDto screeningDto){
        Screening screening = new Screening(screeningDto.startDate(), getMovieFromRequestDto(screeningDto) , getCinemaRoomFromRequestDto(screeningDto), getScreeningTypeFromRequestDto(screeningDto));

        save(screening);

        return screeningToScreeningDto(screening, true, true);
    }

    public void updateScreening(Screening screening, RequestScreeningDto screeningDto){
        screening.setScreeningType(getScreeningTypeFromRequestDto(screeningDto));
        screening.setMovie(getMovieFromRequestDto(screeningDto));
        screening.setCinemaRoom(getCinemaRoomFromRequestDto(screeningDto));
        screening.setStartDate(screeningDto.startDate());

        save(screening);
    }

    private Movie getMovieFromRequestDto(RequestScreeningDto screeningDto){
        return movieService.getMovieById(screeningDto.movieId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This movie does not exist"));
    }

    private CinemaRoom getCinemaRoomFromRequestDto(RequestScreeningDto screeningDto){
        return cinemaRoomService.getCinemaRoomById(screeningDto.roomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This room does not exist"));
    }

    private ScreeningType getScreeningTypeFromRequestDto(RequestScreeningDto screeningDto){
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
