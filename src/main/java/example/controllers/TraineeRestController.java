package example.controllers;

import example.dtos.trainee.TraineeDTO;
import example.entities.Trainee;
import example.mappers.TraineeMapper;
import example.services.TraineeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainees")
public class TraineeRestController {
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;

    public TraineeRestController(TraineeService traineeService, TraineeMapper traineeMapper) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
    }

    @GetMapping
    public ResponseEntity<List<TraineeDTO>> getAllTrainees() {
        List<Trainee> trainees = (List<Trainee>) traineeService.getAllTrainees();
        List<TraineeDTO> traineeDTOList = trainees.stream()
                .map(traineeMapper::traineeToTraineeDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(traineeDTOList);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeDTO> getTraineeByUsername(@PathVariable("username") String username) {
        Optional<Trainee> optionalTrainee = traineeService.getTraineeByUsername(username);
        if(!optionalTrainee.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        TraineeDTO traineeDTO = traineeMapper.traineeToTraineeDTO(optionalTrainee.get());
        return ResponseEntity.ok(traineeDTO);
    }
}
