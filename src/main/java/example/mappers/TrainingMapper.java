package example.mappers;

import example.dtos.training.TrainingBaseDTO;
import example.dtos.training.TrainingCreateDTO;
import example.dtos.training.TrainingTraineeDTO;
import example.dtos.training.TrainingTrainerDTO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.entities.TrainingType;
import example.exceptions.TraineeNotFoundException;
import example.exceptions.TrainerNotFoundException;
import example.services.TraineeService;
import example.services.TrainerService;
import example.services.TrainingTypeService;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingTypeService trainingTypeService;

    public TrainingMapper(TrainerService trainerService, TraineeService traineeService, TrainingTypeService trainingTypeService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingTypeService = trainingTypeService;
    }


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

    public Training trainingCreateDTOToTraining(TrainingCreateDTO trainingCreateDTO) {
        Training training = new Training();

        Trainee trainee = traineeService.getTraineeByUsername(trainingCreateDTO.getTraineeUsername())
                .orElseThrow(() -> new TraineeNotFoundException("There's no trainee with such username"));
        training.setTrainee(trainee);

        TrainingType trainingType = trainingTypeService.findById(trainingCreateDTO.getTrainingType());
        training.setTrainingType(trainingType);

        training.setTrainingName(trainingCreateDTO.getTrainingName());
        training.setTrainingDate(trainingCreateDTO.getTrainingDate());
        training.setTrainingDuration(trainingCreateDTO.getTrainingDuration());

        return training;
    }
}
