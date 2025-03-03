package example.services;

import example.entities.TrainingType;
import example.exceptions.TrainingTypeNotFoundException;
import example.repositories.TrainingTypeRepository;
import example.services.imp.TrainingTypeServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainingTypeServiceTests {
    private TrainingTypeRepository trainingTypeRepository;
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    public void init() {
        trainingTypeRepository = mock(TrainingTypeRepository.class);
        trainingTypeService = new TrainingTypeServiceImp(trainingTypeRepository);
    }

    @Test
    public void findAllTest_ShouldReturnEmptyList() {
        when(trainingTypeRepository.findAll()).thenReturn(new ArrayList<>());

        List<TrainingType> result = trainingTypeService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllTest_ShouldReturnNonEmptyList() {
        TrainingType trainingType = new TrainingType();
        when(trainingTypeRepository.findAll()).thenReturn(Arrays.asList(trainingType));

        List<TrainingType> result = trainingTypeService.findAll();
        assertEquals(1, result.size());
        assertEquals(trainingType, result.get(0));
    }

    @Test
    public void findById_ShouldThrow() {
        when(trainingTypeRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        TrainingTypeNotFoundException exception = assertThrows(TrainingTypeNotFoundException.class,
                () -> trainingTypeService.findById(1L));
        assertEquals("There's no training type with such id: 1", exception.getMessage());
    }

    @Test
    public void findById_ShouldReturnTrainingTypeOptional() {
        TrainingType trainingType = new TrainingType();
        when(trainingTypeRepository.findById(any(Long.class))).thenReturn(Optional.of(trainingType));

        TrainingType result = trainingTypeService.findById(1L);
        assertEquals(trainingType, result);
    }
}
