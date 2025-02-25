package example.repositories;

import example.entities.Trainee;
import example.entities.Training;
import example.repositories.imp.TraineeRepositoryImp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TraineeRepositoryTests {
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private TraineeRepository traineeRepository;
    private Query query;

    @BeforeEach
    public void setUp() {
        entityManager = mock(EntityManager.class);
        transaction = mock(EntityTransaction.class);
        query = mock(Query.class);

        when(entityManager.getTransaction()).thenReturn(transaction);

        traineeRepository = new TraineeRepositoryImp(entityManager);
    }


    @Test
    public void saveIdNullTest_ShouldPersist() {
        Trainee training = new Trainee();

        Trainee savedTrainee = traineeRepository.save(training);

        verify(transaction).begin();
        verify(entityManager).persist(training);
        verify(transaction).commit();
        assertNotNull(savedTrainee);
    }

    @Test
    public void saveIdNotNullTest_ShouldMerge() {
        Trainee training = new Trainee();
        training.setId(1L);

        Trainee savedTrainee = traineeRepository.save(training);

        verify(transaction).begin();
        verify(entityManager).merge(training);
        verify(transaction).commit();
        assertNotNull(savedTrainee);
        assertEquals(1L, savedTrainee.getId());
    }

    @Test
    public void saveExceptionThrownTest_ShouldRollback() {
        Trainee training = new Trainee();
        training.setId(null);

        doThrow(RuntimeException.class).when(entityManager).persist(any(Trainee.class));

        Trainee savedTrainee = traineeRepository.save(training);

        verify(transaction).begin();
        verify(transaction).rollback();
        assertNull(savedTrainee);
    }

    @Test
    public void findAllTest() {
        String jpql = "select t from Trainee t " +
                "left outer join Training tr on t.id=tr.trainee.id";
        Trainee trainee1 = new Trainee();
        Trainee trainee2 = new Trainee();
        List<Trainee> trainees = Arrays.asList(trainee1, trainee2);

        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(trainees);

        List<Trainee> result = traineeRepository.findAll();

        verify(entityManager).createQuery(eq(jpql));
        assertEquals(2, result.size());
        assertEquals(trainee1, result.get(0));
        assertEquals(trainee2, result.get(1));
    }

    @Test
    public void deleteByUsernameTest_ShouldReturnTrue() {
        String jpql = "delete from Trainee t where t.username=:username";
        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.setParameter(any(String.class), any(String.class))).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        boolean result = traineeRepository.deleteByUsername("test");

        verify(transaction).begin();
        verify(entityManager).createQuery(eq(jpql));
        verify(transaction).commit();
        assertTrue(result);
    }

    @Test
    public void deleteByUsernameTest_ShouldReturnFalse() {
        String jpql = "delete from Trainee t where t.username=:username";
        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.setParameter(any(String.class), any(String.class))).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);

        boolean result = traineeRepository.deleteByUsername("test");

        verify(transaction).begin();
        verify(entityManager).createQuery(eq(jpql));
        verify(transaction).commit();
        assertFalse(result);
    }

    @Test
    public void deleteByUsernameTest_ShouldRollback() {
        String jpql = "delete from Trainee t where t.username=:username";
        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);

        doThrow(RuntimeException.class).when(query).executeUpdate();

        boolean result = traineeRepository.deleteByUsername("test");

        verify(transaction).begin();
        verify(transaction).rollback();
        assertFalse(result);
    }

    @Test
    public void findByIdTest_ShouldReturnEmptyOptional() {
        when(entityManager.find(Trainee.class, 1L)).thenReturn(null);

        Optional<Trainee> optionalTrainee = traineeRepository.findById(1L);

        verify(entityManager).find(Trainee.class, 1L);
        assertFalse(optionalTrainee.isPresent());
    }

    @Test
    public void findByIdTest_ShouldReturnOptionalTrainee() {
        Trainee trainee = new Trainee();
        when(entityManager.find(Trainee.class, 1L)).thenReturn(trainee);

        Optional<Trainee> optionalTrainee = traineeRepository.findById(1L);

        verify(entityManager).find(Trainee.class, 1L);
        assertTrue(optionalTrainee.isPresent());
        assertEquals(trainee, optionalTrainee.get());
    }

    @Test
    public void findByUsernameTest_ShouldReturnEmptyOptional() {
        String jpql = "select t from Trainee t where t.username=:username";
        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<Trainee>());

        Optional<Trainee> optionalTrainee = traineeRepository.findByUsername("test");

        verify(entityManager).createQuery(eq(jpql));
        assertFalse(optionalTrainee.isPresent());
    }

    @Test
    public void findByUsernameTest_ShouldReturnOptionalTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUsername("test");
        String jpql = "select t from Trainee t where t.username=:username";

        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(trainee));

        Optional<Trainee> optionalTrainee = traineeRepository.findByUsername("test");

        verify(entityManager).createQuery(eq(jpql));
        assertEquals(trainee, optionalTrainee.get());
        assertEquals("test", optionalTrainee.get().getUsername());
    }

    @Test
    public void findByUsernameAndPasswordTest_ShouldReturnEmptyOptional() {
        String jpql = "select t from Trainee t where t.username=:username and t.password=:password";
        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<Trainee>());

        Optional<Trainee> optionalTrainee = traineeRepository.findByUsernameAndPassword("test", "pass");

        verify(entityManager).createQuery(eq(jpql));
        assertFalse(optionalTrainee.isPresent());
    }

    @Test
    public void findByUsernameAndPasswordTest_ShouldReturnOptionalTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUsername("test");
        trainee.setPassword("pass");
        String jpql = "select t from Trainee t where t.username=:username and t.password=:password";

        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(trainee));

        Optional<Trainee> optionalTrainee = traineeRepository.findByUsernameAndPassword("test", "pass");

        verify(entityManager).createQuery(eq(jpql));
        assertEquals(trainee, optionalTrainee.get());
        assertEquals("test", optionalTrainee.get().getUsername());
        assertEquals("pass", optionalTrainee.get().getPassword());
    }

    @Test
    public void findTrainingListTest_ShouldReturnEmptyList() {
        String jpql = "select t from Training t " +
                "where t.trainee.username=:username " +
                "and (:fromDate is null or t.trainingDate > :fromDate) " +
                "and (:toDate is null or t.trainingDate < :toDate) " +
                "and (:trainerFirstName is null or t.trainer.firstName=:trainerFirstName) " +
                "and (:trainerLastName is null or t.trainer.lastName=:trainerLastName) " +
                "and (:trainingType is null or t.trainingType.id=:trainingType)";
        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<Trainee>());

        List<Training> trainings = traineeRepository.findTrainingList("test", null, null, null, null, null);

        verify(entityManager).createQuery(eq(jpql));
        assertTrue(trainings.isEmpty());
    }

    @Test
    public void findTrainingListTest_ShouldReturnOptionalTrainee() {
        Training training = new Training();
        String jpql = "select t from Training t " +
                "where t.trainee.username=:username " +
                "and (:fromDate is null or t.trainingDate > :fromDate) " +
                "and (:toDate is null or t.trainingDate < :toDate) " +
                "and (:trainerFirstName is null or t.trainer.firstName=:trainerFirstName) " +
                "and (:trainerLastName is null or t.trainer.lastName=:trainerLastName) " +
                "and (:trainingType is null or t.trainingType.id=:trainingType)";

        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(training));

        List<Training> trainings = traineeRepository.findTrainingList("test", null, null, null, null, null);

        verify(entityManager).createQuery(eq(jpql));
        assertEquals(1, trainings.size());
        assertEquals(training, trainings.get(0));
    }
}
