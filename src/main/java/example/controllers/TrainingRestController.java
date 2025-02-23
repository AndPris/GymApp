package example.controllers;

import example.dtos.training.TrainingCreateDTO;
import example.entities.Training;
import example.mappers.TrainingMapper;
import example.services.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainings")
public class TrainingRestController {
    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;

    public TrainingRestController(TrainingService trainingService, TrainingMapper trainingMapper) {
        this.trainingService = trainingService;
        this.trainingMapper = trainingMapper;
    }

    @PostMapping
    public ResponseEntity<?> createTraining(@RequestBody TrainingCreateDTO trainingCreateDTO) {
        Training training = trainingMapper.trainingCreateDTOToTraining(trainingCreateDTO);
        trainingService.createTraining(training);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
