package example.mappers;

import example.dtos.training.TrainingDTO;
import example.entities.Training;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {
    public TrainingDTO trainingToTrainingDTO(Training training) {
        TrainingDTO trainingDTO = new TrainingDTO();

        trainingDTO.setTrainingName(training.getTrainingName());
        trainingDTO.setTrainingDate(training.getTrainingDate());
        trainingDTO.setTrainingType(training.getTrainingType().getId());
        trainingDTO.setTrainingDuration(training.getTrainingDuration());
        trainingDTO.setTrainerFirstName(training.getTrainer().getFirstName());
        trainingDTO.setTrainerLastName(training.getTrainer().getLastName());

        return trainingDTO;
    }
}
