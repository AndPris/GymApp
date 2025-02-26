package example.mappers;

import example.dtos.trainingtype.TrainingTypeDTO;
import example.entities.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingTypeMapperTests {

    private TrainingTypeMapper trainingTypeMapper;

    @BeforeEach
    void setUp() {
        trainingTypeMapper = new TrainingTypeMapper();
    }

    @Test
    void testTrainingTypeToTrainingTypeDTO_ShouldMapCorrectly() {
        TrainingType trainingType = new TrainingType("Strength Training");
        trainingType.setId(1L);

        TrainingTypeDTO trainingTypeDTO = trainingTypeMapper.trainingTypeToTrainingTypeDTO(trainingType);

        assertEquals(1L, trainingTypeDTO.getTrainingTypeId());
        assertEquals("Strength Training", trainingTypeDTO.getTrainingType());
    }
}
