package example.mappers;

import example.dtos.trainee.TraineeCreateDTO;
import example.dtos.trainee.TraineeDTO;
import example.dtos.trainer.TrainerSummaryDTO;
import example.entities.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TraineeMapper extends UserMapper {
    private TrainerMapper trainerMapper;

    @Autowired
    public void setTrainerMapper(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    public TraineeDTO traineeToTraineeDTO(Trainee trainee) {
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setActive(trainee.isActive());
        traineeDTO.setFirstName(trainee.getFirstName());
        traineeDTO.setLastName(trainee.getLastName());
        traineeDTO.setAddress(trainee.getAddress());
        traineeDTO.setDateOfBirth(trainee.getDateOfBirth());

        List<TrainerSummaryDTO> trainers = trainee.getTrainers().stream()
                .map(trainerMapper::trainerToTrainerSummaryDTO)
                .collect(Collectors.toList());

        traineeDTO.setTrainersList(trainers);

        return traineeDTO;
    }


    public Trainee traineeCreateDTOToTrainee(TraineeCreateDTO traineeCreateDTO) {
        Trainee trainee = new Trainee();

        trainee.setFirstName(traineeCreateDTO.getFirstName());
        trainee.setLastName(traineeCreateDTO.getLastName());
        trainee.setAddress(traineeCreateDTO.getAddress());
        trainee.setDateOfBirth(traineeCreateDTO.getDateOfBirth());

        return trainee;
    }
}
