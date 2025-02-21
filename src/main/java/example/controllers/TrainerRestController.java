package example.controllers;

import example.dtos.trainer.TrainerDTO;
import example.entities.Trainer;
import example.mappers.TrainerMapper;
import example.services.TrainerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainers")
public class TrainerRestController {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;

    public TrainerRestController(TrainerService trainerService, TrainerMapper trainerMapper) {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
    }

    @GetMapping
    public ResponseEntity<List<TrainerDTO>> getAllTrainers() {
        List<Trainer> trainers = (List<Trainer>) trainerService.getAllTrainers();

        List<TrainerDTO> trainerDTOS = trainers.stream()
                .map(trainerMapper::trainerToTrainerDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(trainerDTOS);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerDTO> getTrainerByUsername(@PathVariable("username") String username) {
        Optional<Trainer> optionalTrainer = trainerService.getTrainerByUsername(username);
        if (!optionalTrainer.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        TrainerDTO trainerDTO = trainerMapper.trainerToTrainerDTO(optionalTrainer.get());
        return ResponseEntity.ok(trainerDTO);
    }
}
