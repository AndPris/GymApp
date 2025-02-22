package example.controllers;

import example.dtos.CredentialsDTO;
import example.dtos.trainee.TraineeCreateDTO;
import example.dtos.trainee.TraineeDTO;
import example.dtos.trainee.TraineeUpdateDTO;
import example.dtos.trainer.TrainerSummaryDTO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.mappers.TraineeMapper;
import example.mappers.TrainerMapper;
import example.services.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainees")
public class TraineeRestController {
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;

    public TraineeRestController(TraineeService traineeService, TraineeMapper traineeMapper, TrainerMapper trainerMapper) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
    }

    @PostMapping
    public ResponseEntity<CredentialsDTO> createTrainee(@RequestBody TraineeCreateDTO traineeCreateDTO) {
        Trainee trainee = traineeMapper.traineeCreateDTOToTrainee(traineeCreateDTO);
        trainee = traineeService.createTrainee(trainee);
        CredentialsDTO credentialsDTO = traineeMapper.toCredentialsDTO(trainee);
        return ResponseEntity.status(HttpStatus.CREATED).body(credentialsDTO);
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
        if (!optionalTrainee.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        TraineeDTO traineeDTO = traineeMapper.traineeToTraineeDTO(optionalTrainee.get());
        return ResponseEntity.ok(traineeDTO);
    }

    @GetMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerSummaryDTO>> getTrainersList(@PathVariable("username") String username,
                                                                   @RequestParam(defaultValue = "true", name = "inverse") boolean inverse) {
        Optional<Trainee> optionalTrainee = traineeService.getTraineeByUsername(username);
        if(!optionalTrainee.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<Trainer> trainers;

        if(inverse) {
            trainers = traineeService.findTrainersNotAssignedToTrainee(username);
        } else {
            trainers = optionalTrainee.get().getTrainers();
        }

        List<TrainerSummaryDTO> trainerSummaryDTOS = trainers.stream()
                .map(trainerMapper::trainerToTrainerSummaryDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainerSummaryDTOS);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteTraineeByUsername(@PathVariable("username") String username) {
        if (traineeService.deleteTraineeByUsername(username)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeDTO> updateTrainee(@PathVariable("username") String username,
                                                    @RequestBody TraineeUpdateDTO traineeUpdateDTO) {
        Trainee trainee = traineeService.updateTrainee(username, traineeUpdateDTO);
        TraineeDTO traineeDTO = traineeMapper.traineeToTraineeDTO(trainee);
        return ResponseEntity.ok(traineeDTO);
    }
}
