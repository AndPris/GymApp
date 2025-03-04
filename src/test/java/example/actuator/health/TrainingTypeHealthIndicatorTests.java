package example.actuator.health;

import example.dtos.trainingtype.TrainingTypeDTO;
import example.entities.TrainingType;
import example.mappers.TrainingTypeMapper;
import example.repositories.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TrainingTypeHealthIndicatorTests {
    private TrainingTypeRepository trainingTypeRepository;
    private TrainingTypeMapper trainingTypeMapper;
    private TrainingTypeHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {
        trainingTypeRepository = mock(TrainingTypeRepository.class);
        trainingTypeMapper = mock(TrainingTypeMapper.class);
        healthIndicator = new TrainingTypeHealthIndicator(trainingTypeRepository, trainingTypeMapper);
    }

    @Test
    void shouldReturnHealthUpWhenTrainingTypesExist() {
        TrainingType type1 = new TrainingType();
        TrainingType type2 = new TrainingType();

        when(trainingTypeRepository.findAll()).thenReturn(List.of(type1, type2));
        when(trainingTypeMapper.trainingTypeToTrainingTypeDTO(any(TrainingType.class))).thenReturn(new TrainingTypeDTO());

        Health health = healthIndicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertEquals(2, health.getDetails().get("Training Types Count"));
    }

    @Test
    void shouldReturnHealthDownWhenNoTrainingTypesFound() {
        when(trainingTypeRepository.findAll()).thenReturn(Collections.emptyList());

        Health health = healthIndicator.health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertEquals("None found", health.getDetails().get("Training Types"));
    }

    @Test
    void shouldReturnHealthDownOnRepositoryException() {
        when(trainingTypeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Health health = healthIndicator.health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertEquals("Error accessing database", health.getDetails().get("Training Types"));
        assertEquals("Database error", health.getDetails().get("Error"));
    }
}
