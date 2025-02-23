package example.mappers;

import example.dtos.trainingtype.TrainingTypeDTO;
import example.entities.TrainingType;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeMapper {
    public TrainingTypeDTO trainingTypeToTrainingTypeDTO(TrainingType trainingType) {
        TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO();

        trainingTypeDTO.setTrainingTypeId(trainingType.getId());
        trainingTypeDTO.setTrainingType(trainingType.getName());

        return trainingTypeDTO;
    }
}
