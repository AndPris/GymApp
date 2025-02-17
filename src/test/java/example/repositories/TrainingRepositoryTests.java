package example.repositories;

import example.entities.Training;
import example.repositories.imp.TrainingRepositoryImp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainingRepositoryTests {
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private TrainingRepository trainingRepository;
    private Query query;

    @BeforeEach
    public void setUp() {
        entityManager = mock(EntityManager.class);
        transaction = mock(EntityTransaction.class);
        query = mock(Query.class);

        when(entityManager.getTransaction()).thenReturn(transaction);

        trainingRepository = new TrainingRepositoryImp(entityManager);
    }

    @Test
    public void saveIdNullTest_ShouldPersist() {
        Training training = new Training();

        Training savedTraining = trainingRepository.save(training);

        verify(transaction).begin();
        verify(entityManager).persist(training);
        verify(transaction).commit();
        assertNotNull(savedTraining);
    }

    @Test
    public void saveIdNotNullTest_ShouldMerge() {
        Training training = new Training();
        training.setId(1L);

        Training savedTraining = trainingRepository.save(training);

        verify(transaction).begin();
        verify(entityManager).merge(training);
        verify(transaction).commit();
        assertNotNull(savedTraining);
        assertEquals(1L, savedTraining.getId());
    }

    @Test
    public void saveExceptionThrownTest_ShouldRollback() {
        Training training = new Training();
        training.setId(null);

        doThrow(RuntimeException.class).when(entityManager).persist(any(Training.class));

        Training savedTraining = trainingRepository.save(training);

        verify(transaction).begin();
        verify(transaction).rollback();
        assertNull(savedTraining);
    }

    @Test
    public void findAllTest() {
        Training training1 = new Training();
        Training training2 = new Training();
        List<Training> trainings = Arrays.asList(training1, training2);

        when(entityManager.createQuery(eq("select t from Training t"))).thenReturn(query);
        when(query.getResultList()).thenReturn(trainings);

        List<Training> result = trainingRepository.findAll();

        verify(entityManager).createQuery(eq("select t from Training t"));
        assertEquals(2, result.size());
        assertEquals(training1, result.get(0));
        assertEquals(training2, result.get(1));
    }
}
