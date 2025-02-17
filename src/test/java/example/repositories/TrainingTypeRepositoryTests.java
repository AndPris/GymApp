package example.repositories;

import example.entities.TrainingType;
import example.repositories.imp.TrainingTypeRepositoryImp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TrainingTypeRepositoryTests {
    private EntityManager entityManager;
    private TrainingTypeRepository trainingTypeRepository;
    private Query query;

    @BeforeEach
    public void setUp() {
        entityManager = mock(EntityManager.class);
        query = mock(Query.class);
        trainingTypeRepository = new TrainingTypeRepositoryImp(entityManager);
    }

    @Test
    public void findAllTest() {
        TrainingType trainingType1 = new TrainingType();
        TrainingType trainingType2 = new TrainingType();
        List<TrainingType> trainingTypes = Arrays.asList(trainingType1, trainingType2);

        when(entityManager.createQuery(eq("select t from TrainingType t"))).thenReturn(query);
        when(query.getResultList()).thenReturn(trainingTypes);

        List<TrainingType> result = trainingTypeRepository.findAll();

        verify(entityManager).createQuery(eq("select t from TrainingType t"));
        assertEquals(2, result.size());
        assertEquals(trainingType1, result.get(0));
        assertEquals(trainingType2, result.get(1));
    }

    @Test
    public void findByIdTest_ShouldReturnEmptyOptional() {
        when(entityManager.find(TrainingType.class, 1L)).thenReturn(null);

        Optional<TrainingType> optionalTrainingType = trainingTypeRepository.findById(1L);

        verify(entityManager).find(TrainingType.class, 1L);
        assertFalse(optionalTrainingType.isPresent());
    }

    @Test
    public void findByIdTest_ShouldReturnOptionalTrainingType() {
        TrainingType trainingType = new TrainingType();
        when(entityManager.find(TrainingType.class, 1L)).thenReturn(trainingType);

        Optional<TrainingType> optionalTrainingType = trainingTypeRepository.findById(1L);

        verify(entityManager).find(TrainingType.class, 1L);
        assertTrue(optionalTrainingType.isPresent());
        assertEquals(trainingType, optionalTrainingType.get());
    }
}
