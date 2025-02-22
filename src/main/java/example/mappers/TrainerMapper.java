package example.mappers;

import example.dtos.trainee.TraineeSummaryDTO;
import example.dtos.trainer.TrainerCreateDTO;
import example.dtos.trainer.TrainerDTO;
import example.dtos.trainer.TrainerSummaryDTO;
import example.entities.Trainer;
import example.entities.TrainingType;
import example.services.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainerMapper extends UserMapper {
    private TraineeMapper traineeMapper;
    private final TrainingTypeService trainingTypeService;

    public TrainerMapper(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @Autowired
    public void setTraineeMapper(TraineeMapper traineeMapper) {
        this.traineeMapper = traineeMapper;
    }


    public TrainerDTO trainerToTrainerDTO(Trainer trainer) {
        TrainerDTO trainerDTO = new TrainerDTO();

        trainerDTO.setActive(trainer.isActive());
        trainerDTO.setFirstName(trainer.getFirstName());
        trainerDTO.setLastName(trainer.getLastName());
        trainerDTO.setSpecialization(trainer.getSpecialization().getId());

        List<TraineeSummaryDTO> trainees = trainer.getTrainees().stream()
                .map(traineeMapper::traineeToTraineeSummaryDTO)
                .collect(Collectors.toList());

        trainerDTO.setTraineesList(trainees);

        return trainerDTO;
    }

    public TrainerSummaryDTO trainerToTrainerSummaryDTO(Trainer trainer) {
        TrainerSummaryDTO trainerSummaryDTO = new TrainerSummaryDTO();

        trainerSummaryDTO.setFirstName(trainer.getFirstName());
        trainerSummaryDTO.setLastName(trainer.getLastName());
        trainerSummaryDTO.setUsername(trainer.getUsername());
        trainerSummaryDTO.setSpecialization(trainer.getSpecialization().getId());

        return trainerSummaryDTO;
    }

    public Trainer trainerCreateDTOToTrainer(TrainerCreateDTO trainerCreateDTO) {
        Trainer trainer = new Trainer();

        trainer.setFirstName(trainerCreateDTO.getFirstName());
        trainer.setLastName(trainerCreateDTO.getLastName());
        TrainingType trainingType = trainingTypeService.findById(trainerCreateDTO.getSpecialization());
        trainer.setSpecialization(trainingType);

        return trainer;
    }
}
