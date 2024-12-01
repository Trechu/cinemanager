package pl.edu.agh.to.cinemanager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to.cinemanager.service.CinemaRoomService;

@RestController
@RequestMapping(path = "cinemaRooms")
public class CinemaRoomController {

    private final CinemaRoomService cinemaRoomService;

    public CinemaRoomController(CinemaRoomService cinemaRoomService) {
        this.cinemaRoomService = cinemaRoomService;
    }
}
