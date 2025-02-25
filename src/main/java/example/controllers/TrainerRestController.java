package example.controllers;

import example.dtos.ActiveStatusDTO;
import example.dtos.CredentialsDTO;
import example.dtos.trainer.TrainerCreateDTO;
import example.dtos.trainer.TrainerDTO;
import example.dtos.trainer.TrainerUpdateDTO;
import example.dtos.training.TrainingBaseDTO;
import example.dtos.training.TrainingCreateDTO;
import example.dtos.training.TrainingTrainerDTO;
import example.entities.Trainer;
import example.entities.Training;
import example.exceptions.TrainerNotFoundException;
import example.mappers.TrainerMapper;
import example.mappers.TrainingMapper;
import example.security.annotations.Authenticated;
import example.security.annotations.Authorized;
import example.services.TrainerService;
import example.services.TrainingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainers")
public class TrainerRestController {
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;

    public TrainerRestController(TrainerService trainerService, TrainingService trainingService,
                                 TrainerMapper trainerMapper, TrainingMapper trainingMapper) {
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
    }

    @PostMapping
    public ResponseEntity<CredentialsDTO> createTrainer(@RequestBody TrainerCreateDTO trainerCreateDTO) {
        Trainer trainer = trainerMapper.trainerCreateDTOToTrainer(trainerCreateDTO);
        trainer = trainerService.createTrainer(trainer);
        CredentialsDTO credentialsDTO = trainerMapper.toCredentialsDTO(trainer);
        return ResponseEntity.status(HttpStatus.CREATED).body(credentialsDTO);
    }

    @GetMapping
    public ResponseEntity<List<TrainerDTO>> getAllTrainers() {
        List<Trainer> trainers = (List<Trainer>) trainerService.getAllTrainers();

        List<TrainerDTO> trainerDTOS = trainers.stream()
                .map(trainerMapper::trainerToTrainerDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(trainerDTOS);
    }

    @Authenticated
    @GetMapping("/{username}")
    public ResponseEntity<TrainerDTO> getTrainerByUsername(@PathVariable("username") String username,
                                                           @RequestHeader(value = "Authorization") String authHeader) {
        Optional<Trainer> optionalTrainer = trainerService.getTrainerByUsername(username);
        if (!optionalTrainer.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        TrainerDTO trainerDTO = trainerMapper.trainerToTrainerDTO(optionalTrainer.get());
        return ResponseEntity.ok(trainerDTO);
    }

    @Authenticated
    @Authorized
    @PostMapping("/{username}/trainings")
    public ResponseEntity<?> createTraining(@PathVariable("username") String username,
                                            @RequestBody TrainingCreateDTO trainingCreateDTO,
                                            @RequestHeader(value = "Authorization") String authHeader) {
        Training training = trainingMapper.trainingCreateDTOToTraining(trainingCreateDTO);
        Trainer trainer = trainerService.getTrainerByUsername(username)
                .orElseThrow(() -> new TrainerNotFoundException("There's no trainer with such username: " + username));
        training.setTrainer(trainer);

        trainingService.createTraining(training);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Authenticated
    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingTrainerDTO>> getTrainingsList(
            @PathVariable("username") String username,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date to,
            @RequestParam(name = "traineeFirstName", required = false) String traineeFirstName,
            @RequestParam(name = "traineeLastName", required = false) String traineeLastName,
            @RequestHeader(value = "Authorization") String authHeader) {

        if (!trainerService.getTrainerByUsername(username).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<Training> trainings = trainerService.findTrainerTrainingList(username, from, to,
                traineeFirstName, traineeLastName);

        List<TrainingTrainerDTO> trainingBaseDTOS = trainings.stream()
                .map(trainingMapper::trainingToTrainingTrainerDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainingBaseDTOS);
    }

    @Authenticated
    @Authorized
    @PatchMapping("/{username}/active")
    public ResponseEntity<?> toggleTrainerActiveStatus(@PathVariable("username") String username,
                                                       @RequestHeader(value = "Authorization") String authHeader) {
        Boolean active = trainerService.toggleTrainerIsActiveStatus(username);
        return ResponseEntity.ok(new ActiveStatusDTO(active));
    }

    @Authenticated
    @Authorized
    @PutMapping("/{username}")
    public ResponseEntity<TrainerDTO> updateTrainer(@PathVariable("username") String username,
                                                    @RequestBody TrainerUpdateDTO trainerUpdateDTO,
                                                    @RequestHeader(value = "Authorization") String authHeader) {
        Trainer trainer = trainerService.updateTrainer(username, trainerUpdateDTO);
        TrainerDTO trainerDTO = trainerMapper.trainerToTrainerDTO(trainer);
        return ResponseEntity.ok(trainerDTO);
    }
}
