package example.mappers;

import example.dtos.trainer.TrainerSummaryDTO;
import example.entities.Trainer;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper extends UserMapper {
    public TrainerSummaryDTO trainerToTrainerSummaryDTO(Trainer trainer) {
        TrainerSummaryDTO trainerSummaryDTO = new TrainerSummaryDTO();

        trainerSummaryDTO.setFirstName(trainer.getFirstName());
        trainerSummaryDTO.setLastName(trainer.getLastName());
        trainerSummaryDTO.setUsername(trainer.getUsername());
        trainerSummaryDTO.setSpecialization(trainer.getSpecialization().getId());

        return trainerSummaryDTO;
    }
}
