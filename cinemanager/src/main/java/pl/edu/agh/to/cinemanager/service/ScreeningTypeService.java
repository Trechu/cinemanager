package pl.edu.agh.to.cinemanager.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.cinemanager.repository.ScreeningTypeRepository;

@Service
public class ScreeningTypeService {

    private final ScreeningTypeRepository screeningTypeRepository;

    public ScreeningTypeService(ScreeningTypeRepository screeningTypeRepository) {
        this.screeningTypeRepository = screeningTypeRepository;
    }
}
