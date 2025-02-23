package example.controllers;

import example.dtos.trainingtype.TrainingTypeDTO;
import example.entities.TrainingType;
import example.mappers.TrainingTypeMapper;
import example.services.TrainingTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/training-types")
public class TrainingTypeRestController {
    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingTypeRestController(TrainingTypeService trainingTypeService, TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeMapper = trainingTypeMapper;
    }


    @GetMapping
    public ResponseEntity<List<TrainingTypeDTO>> getTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeService.findAll();

        List<TrainingTypeDTO> trainingTypeDTOS = trainingTypes.stream()
                .map(trainingTypeMapper::trainingTypeToTrainingTypeDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(trainingTypeDTOS);
    }
}
