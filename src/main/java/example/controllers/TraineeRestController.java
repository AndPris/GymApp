package example.controllers;

import example.dtos.ActiveStatusDTO;
import example.dtos.CredentialsDTO;
import example.dtos.trainee.TraineeCreateDTO;
import example.dtos.trainee.TraineeDTO;
import example.dtos.trainee.TraineeTrainerListUpdateDTO;
import example.dtos.trainee.TraineeUpdateDTO;
import example.dtos.trainer.TrainerSummaryDTO;
import example.dtos.training.TrainingBaseDTO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.mappers.TraineeMapper;
import example.mappers.TrainerMapper;
import example.mappers.TrainingMapper;
import example.services.TraineeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainees")
public class TraineeRestController {
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;

    public TraineeRestController(TraineeService traineeService, TraineeMapper traineeMapper,
                                 TrainerMapper trainerMapper, TrainingMapper trainingMapper) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
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
    public ResponseEntity<List<TrainerSummaryDTO>> getActiveTrainersList(@PathVariable("username") String username,
                                                                   @RequestParam(defaultValue = "true", name = "inverse") boolean inverse) {
        Optional<Trainee> optionalTrainee = traineeService.getTraineeByUsername(username);
        if (!optionalTrainee.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<Trainer> trainers;

        if (inverse) {
            trainers = traineeService.findActiveTrainersNotAssignedToTrainee(username);
        } else {
            trainers = optionalTrainee.get().getTrainers().stream()
                    .filter(Trainer::isActive)
                    .collect(Collectors.toList());
        }

        List<TrainerSummaryDTO> trainerSummaryDTOS = trainers.stream()
                .map(trainerMapper::trainerToTrainerSummaryDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainerSummaryDTOS);
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingBaseDTO>> getTrainingsList(
            @PathVariable("username") String username,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date to,
            @RequestParam(name = "trainerFirstName", required = false) String trainerFirstName,
            @RequestParam(name = "trainerLastName", required = false) String trainerLastName,
            @RequestParam(name = "trainingType", required = false) Long trainingType) {

        if (!traineeService.getTraineeByUsername(username).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<Training> trainings = traineeService.findTraineeTrainingList(username, from, to,
                trainerFirstName, trainerLastName, trainingType);

        List<TrainingBaseDTO> trainingBaseDTOS = trainings.stream()
                .map(trainingMapper::trainingToTrainingTraineeDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainingBaseDTOS);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteTraineeByUsername(@PathVariable("username") String username) {
        if (traineeService.deleteTraineeByUsername(username)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{username}/active")
    public ResponseEntity<?> toggleTraineeActiveStatus(@PathVariable("username") String username) {
        Boolean active = traineeService.toggleTraineeIsActiveStatus(username);
        return ResponseEntity.ok(new ActiveStatusDTO(active));
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeDTO> updateTrainee(@PathVariable("username") String username,
                                                    @RequestBody TraineeUpdateDTO traineeUpdateDTO) {
        Trainee trainee = traineeService.updateTrainee(username, traineeUpdateDTO);
        TraineeDTO traineeDTO = traineeMapper.traineeToTraineeDTO(trainee);
        return ResponseEntity.ok(traineeDTO);
    }

    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerSummaryDTO>> updateTraineeTrainerList(@PathVariable("username") String username,
                                                                            @RequestBody TraineeTrainerListUpdateDTO traineeTrainerListUpdateDTO) {
        Optional<Trainee> optionalTrainee = traineeService.getTraineeByUsername(username);
        if (!optionalTrainee.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<Trainer> trainers = traineeService.updateTraineeTrainerList(username, traineeTrainerListUpdateDTO);
        List<TrainerSummaryDTO> trainerSummaryDTOS = trainers.stream()
                .map(trainerMapper::trainerToTrainerSummaryDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(trainerSummaryDTOS);
    }
}
