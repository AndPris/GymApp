package example.repositories;

import example.entities.Trainee;
import example.entities.User;
import example.repositories.imp.UserRepositoryImp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserRepositoryTests {
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private UserRepository userRepository;
    private Query query;

    @BeforeEach
    public void setUp() {
        entityManager = mock(EntityManager.class);
        transaction = mock(EntityTransaction.class);
        query = mock(Query.class);

        when(entityManager.getTransaction()).thenReturn(transaction);

        userRepository = new UserRepositoryImp(entityManager);
    }

    @Test
    public void saveIdNullTest_ShouldPersist() {
        Trainee user = new Trainee();

        User savedUser = userRepository.save(user);

        verify(transaction).begin();
        verify(entityManager).persist(user);
        verify(transaction).commit();
        assertNotNull(savedUser);
    }

    @Test
    public void saveIdNotNullTest_ShouldMerge() {
        Trainee user = new Trainee();
        user.setId(1L);

        User savedUser = userRepository.save(user);

        verify(transaction).begin();
        verify(entityManager).merge(user);
        verify(transaction).commit();
        assertNotNull(savedUser);
        assertEquals(1L, savedUser.getId());
    }

    @Test
    public void saveExceptionThrownTest_ShouldRollback() {
        Trainee user = new Trainee();
        user.setId(null);

        doThrow(RuntimeException.class).when(entityManager).persist(any(User.class));

        User savedUser = userRepository.save(user);

        verify(transaction).begin();
        verify(transaction).rollback();
        assertNull(savedUser);
    }

    @Test
    public void findByUsernameAndPasswordTest_ShouldReturnEmptyOptional() {
        String jpql = "select u from User u where u.username=:username and u.password=:password";
        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<User>());

        Optional<User> optionalUser = userRepository.findByUsernameAndPassword("test", "pass");

        verify(entityManager).createQuery(eq(jpql));
        assertFalse(optionalUser.isPresent());
    }

    @Test
    public void findByUsernameAndPasswordTest_ShouldReturnOptionalUser() {
        User user = new Trainee();
        user.setUsername("test");
        user.setPassword("pass");
        String jpql = "select u from User u where u.username=:username and u.password=:password";

        when(entityManager.createQuery(eq(jpql))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(user));

        Optional<User> optionalUser = userRepository.findByUsernameAndPassword("test", "pass");

        verify(entityManager).createQuery(eq(jpql));
        assertEquals(user, optionalUser.get());
        assertEquals("test", optionalUser.get().getUsername());
        assertEquals("pass", optionalUser.get().getPassword());
    }
}
