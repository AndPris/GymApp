package example.mappers;

import example.dtos.training.TrainingBaseDTO;
import example.dtos.training.TrainingTraineeDTO;
import example.dtos.training.TrainingTrainerDTO;
import example.entities.Training;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {
    public TrainingTraineeDTO trainingToTrainingTraineeDTO(Training training) {
        TrainingTraineeDTO trainingTraineeDTO = new TrainingTraineeDTO();

        populateTrainingBaseDTOFields(training, trainingTraineeDTO);
        trainingTraineeDTO.setTrainerFirstName(training.getTrainer().getFirstName());
        trainingTraineeDTO.setTrainerLastName(training.getTrainer().getLastName());

        return trainingTraineeDTO;
    }

    public TrainingTrainerDTO trainingToTrainingTrainerDTO(Training training) {
        TrainingTrainerDTO trainingTrainerDTO = new TrainingTrainerDTO();

        populateTrainingBaseDTOFields(training, trainingTrainerDTO);
        trainingTrainerDTO.setTraineeFirstName(training.getTrainee().getFirstName());
        trainingTrainerDTO.setTraineeLastName(training.getTrainee().getLastName());

        return trainingTrainerDTO;
    }

    private void populateTrainingBaseDTOFields(Training training, TrainingBaseDTO trainingBaseDTO) {
        trainingBaseDTO.setTrainingName(training.getTrainingName());
        trainingBaseDTO.setTrainingDate(training.getTrainingDate());
        trainingBaseDTO.setTrainingType(training.getTrainingType().getId());
        trainingBaseDTO.setTrainingDuration(training.getTrainingDuration());
    }
}
