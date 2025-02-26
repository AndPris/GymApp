package example.controllers;

import example.dtos.ActiveStatusDTO;
import example.dtos.CredentialsDTO;
import example.dtos.trainee.TraineeCreateDTO;
import example.dtos.trainee.TraineeDTO;
import example.dtos.trainee.TraineeTrainerListUpdateDTO;
import example.dtos.trainee.TraineeUpdateDTO;
import example.dtos.trainer.TrainerSummaryDTO;
import example.dtos.training.TrainingTraineeDTO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.mappers.TraineeMapper;
import example.mappers.TrainerMapper;
import example.mappers.TrainingMapper;
import example.security.annotations.Authenticated;
import example.security.annotations.Authorized;
import example.services.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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


    @Operation(summary = "Create new trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee successfully created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CredentialsDTO.class)) }),
            @ApiResponse(responseCode = "422", description = "Invalid request body",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CredentialsDTO> createTrainee(@RequestBody TraineeCreateDTO traineeCreateDTO) {
        Trainee trainee = traineeMapper.traineeCreateDTOToTrainee(traineeCreateDTO);
        trainee = traineeService.createTrainee(trainee);
        CredentialsDTO credentialsDTO = traineeMapper.toCredentialsDTO(trainee);
        return ResponseEntity.status(HttpStatus.CREATED).body(credentialsDTO);
    }


    @Operation(summary = "Get all trainees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All trainees are returned",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TraineeDTO.class))) })
    })
    @GetMapping
    public ResponseEntity<List<TraineeDTO>> getAllTrainees() {
        List<Trainee> trainees = (List<Trainee>) traineeService.getAllTrainees();
        List<TraineeDTO> traineeDTOList = trainees.stream()
                .map(traineeMapper::traineeToTraineeDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(traineeDTOList);
    }


    @Operation(summary = "Get trainee by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TraineeDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content)
    })
    @Authenticated
    @GetMapping("/{username}")
    public ResponseEntity<TraineeDTO> getTraineeByUsername(@PathVariable("username") String username,
                                                           @RequestHeader(value = "Authorization") String authHeader) {
        Optional<Trainee> optionalTrainee = traineeService.getTraineeByUsername(username);
        if (!optionalTrainee.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        TraineeDTO traineeDTO = traineeMapper.traineeToTraineeDTO(optionalTrainee.get());
        return ResponseEntity.ok(traineeDTO);
    }


    @Operation(summary = "Get favourite active trainers list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainers list is returned",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TrainerSummaryDTO.class))) }),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No trainee with such username",
                    content = @Content)
    })
    @Authenticated
    @GetMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerSummaryDTO>> getActiveTrainersList(@PathVariable("username") String username,
                                                                         @RequestParam(defaultValue = "true", name = "inverse") boolean inverse,
                                                                         @RequestHeader(value = "Authorization") String authHeader) {
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


    @Operation(summary = "Get trainee's trainings list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings list is returned",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TrainingTraineeDTO.class))) }),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No trainee with such username",
                    content = @Content)
    })
    @Authenticated
    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingTraineeDTO>> getTrainingsList(
            @PathVariable("username") String username,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date to,
            @RequestParam(name = "trainerFirstName", required = false) String trainerFirstName,
            @RequestParam(name = "trainerLastName", required = false) String trainerLastName,
            @RequestParam(name = "trainingType", required = false) Long trainingType,
            @RequestHeader(value = "Authorization") String authHeader) {

        if (!traineeService.getTraineeByUsername(username).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<Training> trainings = traineeService.findTraineeTrainingList(username, from, to,
                trainerFirstName, trainerLastName, trainingType);

        List<TrainingTraineeDTO> trainingBaseDTOS = trainings.stream()
                .map(trainingMapper::trainingToTrainingTraineeDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainingBaseDTOS);
    }


    @Operation(summary = "Delete trainee by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Authorization failed",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No trainee with such username",
                    content = @Content)
    })
    @Authenticated
    @Authorized
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteTraineeByUsername(@PathVariable("username") String username,
                                                     @RequestHeader(value = "Authorization") String authHeader) {
        if (traineeService.deleteTraineeByUsername(username)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Toggle trainee's active status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status successfully changed",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActiveStatusDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Authorization failed",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No trainee with such username",
                    content = @Content)
    })
    @Authenticated
    @Authorized
    @PatchMapping("/{username}/active")
    public ResponseEntity<ActiveStatusDTO> toggleTraineeActiveStatus(@PathVariable("username") String username,
                                                       @RequestHeader(value = "Authorization") String authHeader) {
        Boolean active = traineeService.toggleTraineeIsActiveStatus(username);
        return ResponseEntity.ok(new ActiveStatusDTO(active));
    }


    @Operation(summary = "Update trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee successfully updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TraineeDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Authorization failed",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No trainee with such username",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid request body",
                    content = @Content)
    })
    @Authenticated
    @Authorized
    @PutMapping("/{username}")
    public ResponseEntity<TraineeDTO> updateTrainee(@PathVariable("username") String username,
                                                    @RequestBody TraineeUpdateDTO traineeUpdateDTO,
                                                    @RequestHeader(value = "Authorization") String authHeader) {
        Trainee trainee = traineeService.updateTrainee(username, traineeUpdateDTO);
        TraineeDTO traineeDTO = traineeMapper.traineeToTraineeDTO(trainee);
        return ResponseEntity.ok(traineeDTO);
    }


    @Operation(summary = "Update trainee's favourite trainers list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainers list successfully updated",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TrainerSummaryDTO.class))) }),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Authorization failed",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No trainee with such username",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid request body",
                    content = @Content)
    })
    @Authenticated
    @Authorized
    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerSummaryDTO>> updateTraineeTrainerList(@PathVariable("username") String username,
                                                                            @RequestBody TraineeTrainerListUpdateDTO traineeTrainerListUpdateDTO,
                                                                            @RequestHeader(value = "Authorization") String authHeader) {
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
