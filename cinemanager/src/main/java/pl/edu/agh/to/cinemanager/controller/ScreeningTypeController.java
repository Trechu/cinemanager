package pl.edu.agh.to.cinemanager.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to.cinemanager.service.ScreeningTypeService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/screening-type")
public class ScreeningTypeController {

    private final ScreeningTypeService screeningTypeService;

    public ScreeningTypeController(ScreeningTypeService screeningTypeService) {
        this.screeningTypeService = screeningTypeService;
    }
}
