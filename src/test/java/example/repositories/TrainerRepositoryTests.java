package example.repositories;

import example.entities.Trainer;
import example.entities.Training;
import example.repositories.imp.TrainerRepositoryImp;
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

public class TrainerRepositoryTests {
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private TrainerRepository trainerRepository;
    private Query query;

    @BeforeEach
    public void setUp() {
        entityManager = mock(EntityManager.class);
        transaction = mock(EntityTransaction.class);
        query = mock(Query.class);

        when(entityManager.getTransaction()).thenReturn(transaction);

        trainerRepository = new TrainerRepositoryImp(entityManager);
    }


    @Test
    public void saveIdNullTest_ShouldPersist() {
        Trainer training = new Trainer();

        Trainer savedTrainer = trainerRepository.save(training);

        verify(transaction).begin();
        verify(entityManager).persist(training);
        verify(transaction).commit();
        assertNotNull(savedTrainer);
    }

    @Test
    public void saveIdNotNullTest_ShouldMerge() {
        Trainer training = new Trainer();
        training.setId(1L);

        Trainer savedTrainer = trainerRepository.save(training);

        verify(transaction).begin();
        verify(entityManager).merge(training);
        verify(transaction).commit();
        assertNotNull(savedTrainer);
        assertEquals(1L, savedTrainer.getId());
    }

    @Test
    public void saveExceptionThrownTest_ShouldRollback() {
        Trainer training = new Trainer();
        training.setId(null);

        doThrow(RuntimeException.class).when(entityManager).persist(any(Trainer.class));

        Trainer savedTrainer = trainerRepository.save(training);

        verify(transaction).begin();
        verify(transaction).rollback();
        assertNull(savedTrainer);
    }

    @Test
    public void findAllTest() {
        String jpql = "select t from Trainer t " +
                "left outer join Training tr on t.id=tr.trainee.id";
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();
        List<Trainer> trainers = Arrays.asList(trainer1, trainer2);

        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(trainers);

        List<Trainer> result = trainerRepository.findAll();

        verify(entityManager).createQuery(eq(jpql));
        assertEquals(2, result.size());
        assertEquals(trainer1, result.get(0));
        assertEquals(trainer2, result.get(1));
    }

    @Test
    public void findByIdTest_ShouldReturnEmptyOptional() {
        when(entityManager.find(Trainer.class, 1L)).thenReturn(null);

        Optional<Trainer> optionalTrainer = trainerRepository.findById(1L);

        verify(entityManager).find(Trainer.class, 1L);
        assertFalse(optionalTrainer.isPresent());
    }

    @Test
    public void findByIdTest_ShouldReturnOptionalTrainer() {
        Trainer trainer = new Trainer();
        when(entityManager.find(Trainer.class, 1L)).thenReturn(trainer);

        Optional<Trainer> optionalTrainer = trainerRepository.findById(1L);

        verify(entityManager).find(Trainer.class, 1L);
        assertTrue(optionalTrainer.isPresent());
        assertEquals(trainer, optionalTrainer.get());
    }

    @Test
    public void findByUsernameTest_ShouldReturnEmptyOptional() {
        String jpql = "select t from Trainer t where t.username=:username";
        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<Trainer>());

        Optional<Trainer> optionalTrainer = trainerRepository.findByUsername("test");

        verify(entityManager).createQuery(eq(jpql));
        assertFalse(optionalTrainer.isPresent());
    }

    @Test
    public void findByUsernameTest_ShouldReturnOptionalTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUsername("test");
        String jpql = "select t from Trainer t where t.username=:username";

        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(trainer));

        Optional<Trainer> optionalTrainer = trainerRepository.findByUsername("test");

        verify(entityManager).createQuery(eq(jpql));
        assertEquals(trainer, optionalTrainer.get());
        assertEquals("test", optionalTrainer.get().getUsername());
    }

    @Test
    public void findByUsernameAndPasswordTest_ShouldReturnEmptyOptional() {
        String jpql = "select t from Trainer t where t.username=:username and t.password=:password";
        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<Trainer>());

        Optional<Trainer> optionalTrainer = trainerRepository.findByUsernameAndPassword("test", "pass");

        verify(entityManager).createQuery(eq(jpql));
        assertFalse(optionalTrainer.isPresent());
    }

    @Test
    public void findByUsernameAndPasswordTest_ShouldReturnOptionalTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUsername("test");
        trainer.setPassword("pass");
        String jpql = "select t from Trainer t where t.username=:username and t.password=:password";

        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(trainer));

        Optional<Trainer> optionalTrainer = trainerRepository.findByUsernameAndPassword("test", "pass");

        verify(entityManager).createQuery(eq(jpql));
        assertEquals(trainer, optionalTrainer.get());
        assertEquals("test", optionalTrainer.get().getUsername());
        assertEquals("pass", optionalTrainer.get().getPassword());
    }

    @Test
    public void findTrainingListTest_ShouldReturnEmptyList() {
        String jpql = "select t from Training t " +
                "where t.trainer.username=:username " +
                "and (:fromDate is null or t.trainingDate > :fromDate) " +
                "and (:toDate is null or t.trainingDate < :toDate) " +
                "and (:traineeFirstName is null or t.trainee.firstName=:traineeFirstName) " +
                "and (:traineeLastName is null or t.trainee.lastName=:traineeLastName)";
        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<Trainer>());

        List<Training> trainings = trainerRepository.findTrainingList("test", null, null, null, null);

        verify(entityManager).createQuery(eq(jpql));
        assertTrue(trainings.isEmpty());
    }

    @Test
    public void findTrainingListTest_ShouldReturnOptionalTrainer() {
        Training training = new Training();
        String jpql = "select t from Training t " +
                "where t.trainer.username=:username " +
                "and (:fromDate is null or t.trainingDate > :fromDate) " +
                "and (:toDate is null or t.trainingDate < :toDate) " +
                "and (:traineeFirstName is null or t.trainee.firstName=:traineeFirstName) " +
                "and (:traineeLastName is null or t.trainee.lastName=:traineeLastName)";

        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(training));

        List<Training> trainings = trainerRepository.findTrainingList("test", null, null, null, null);

        verify(entityManager).createQuery(eq(jpql));
        assertEquals(1, trainings.size());
        assertEquals(training, trainings.get(0));
    }
}
