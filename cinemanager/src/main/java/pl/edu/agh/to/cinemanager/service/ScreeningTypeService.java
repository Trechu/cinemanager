package pl.edu.agh.to.cinemanager.service;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestScreeningTypeDto;
import pl.edu.agh.to.cinemanager.dto.ResponseScreeningTypeDto;
import pl.edu.agh.to.cinemanager.model.ScreeningType;
import pl.edu.agh.to.cinemanager.repository.ScreeningTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ScreeningTypeService {

    private final ScreeningTypeRepository screeningTypeRepository;

    public ScreeningTypeService(ScreeningTypeRepository screeningTypeRepository) {
        this.screeningTypeRepository = screeningTypeRepository;
    }

    public List<ResponseScreeningTypeDto> getAllScreeningTypes(){
        return screeningTypeRepository.findAll().stream().map(this::screeningTypeToScreeningTypeDto).toList();
    }

    public Optional<ScreeningType> getScreeningTypeById(Long id){
        return screeningTypeRepository.findById(id);
    }

    public ResponseScreeningTypeDto screeningTypeToScreeningTypeDto(ScreeningType screeningType){
        return new ResponseScreeningTypeDto(screeningType.getId(), screeningType.getName(), screeningType.getBasePrice(), screeningType.getDiscountPrice());
    }

    public ResponseScreeningTypeDto createScreeningType(RequestScreeningTypeDto requestScreeningTypeDto){
        ScreeningType screeningType = new ScreeningType(
                requestScreeningTypeDto.name(),
                requestScreeningTypeDto.base_price(),
                requestScreeningTypeDto.discount_price()
        );
        save(screeningType);

        return screeningTypeToScreeningTypeDto(screeningType);
    }

    public void updateScreeningType(ScreeningType screeningType, RequestScreeningTypeDto updatedScreeningType){
        screeningType.setName(updatedScreeningType.name());
        screeningType.setBasePrice(updatedScreeningType.base_price());
        screeningType.setDiscountPrice(updatedScreeningType.discount_price());

        save(screeningType);
    }

    private void save(ScreeningType screeningType) {
        try {
            screeningTypeRepository.save(screeningType);
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
