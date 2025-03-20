package example.controllers;

import example.dtos.trainingtype.TrainingTypeDTO;
import example.entities.TrainingType;
import example.mappers.TrainingTypeMapper;
import example.services.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TrainingTypeRestControllerTests {
    @InjectMocks
    private TrainingTypeRestController trainingTypeRestController;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainingTypeMapper trainingTypeMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTrainingTypes_ShouldReturnListOfDTOs() {
        TrainingType type1 = new TrainingType();
        TrainingType type2 = new TrainingType();
        when(trainingTypeService.findAll()).thenReturn(Arrays.asList(type1, type2));

        TrainingTypeDTO dto1 = new TrainingTypeDTO();
        TrainingTypeDTO dto2 = new TrainingTypeDTO();
        when(trainingTypeMapper.trainingTypeToTrainingTypeDTO(type1)).thenReturn(dto1);
        when(trainingTypeMapper.trainingTypeToTrainingTypeDTO(type2)).thenReturn(dto2);

        ResponseEntity<List<TrainingTypeDTO>> response = trainingTypeRestController.getTrainingTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<TrainingTypeDTO> dtos = response.getBody();
        assertEquals(2, dtos.size());
        assertEquals(dto1, dtos.get(0));
        assertEquals(dto2, dtos.get(1));
    }
}
