package pl.edu.agh.to.cinemanager.service;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestCinemaRoomDto;
import pl.edu.agh.to.cinemanager.dto.ResponseCinemaRoomDto;
import pl.edu.agh.to.cinemanager.model.CinemaRoom;
import pl.edu.agh.to.cinemanager.repository.CinemaRoomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaRoomService {

    private final CinemaRoomRepository cinemaRoomRepository;

    public CinemaRoomService(CinemaRoomRepository cinemaRoomRepository) {
        this.cinemaRoomRepository = cinemaRoomRepository;
    }

    public List<ResponseCinemaRoomDto> getAllRooms(){
        return cinemaRoomRepository.findAll().stream().map(this::cinemaRoomToCinemaRoomResponseDto).toList();
    }

    public Optional<CinemaRoom> getCinemaRoomById(Long id){
        return cinemaRoomRepository.findById(id);
    }

    public ResponseCinemaRoomDto cinemaRoomToCinemaRoomResponseDto(CinemaRoom cinemaRoom){
        return new ResponseCinemaRoomDto(cinemaRoom.getId(), cinemaRoom.getName(), cinemaRoom.getRows(), cinemaRoom.getSeatsPerRow());
    }

    public ResponseCinemaRoomDto createCinemaRoom(RequestCinemaRoomDto requestCinemaRoomDto){
        CinemaRoom cinemaRoom = new CinemaRoom(requestCinemaRoomDto.name(), requestCinemaRoomDto.rows(), requestCinemaRoomDto.seats_per_row());
        save(cinemaRoom);

        return cinemaRoomToCinemaRoomResponseDto(cinemaRoom);
    }

    public void updateCinemaRoom(CinemaRoom cinemaRoom, RequestCinemaRoomDto requestCinemaRoomDto) {
        cinemaRoom.setName(requestCinemaRoomDto.name());
        cinemaRoom.setRows(requestCinemaRoomDto.rows());
        cinemaRoom.setSeatsPerRow(requestCinemaRoomDto.seats_per_row());

        save(cinemaRoom);
    }

    private void save(CinemaRoom cinemaRoom) {
        try {
            cinemaRoomRepository.save(cinemaRoom);
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
