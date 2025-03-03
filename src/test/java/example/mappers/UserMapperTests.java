package example.mappers;

import example.dtos.CredentialsDTO;
import example.entities.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTests {

    private TrainerMapper trainerMapper;

    @BeforeEach
    void setUp() {
        trainerMapper = new TrainerMapper(null);
    }

    @Test
    void toCredentialsDTO_ShouldMapTrainerToCredentialsDTO() {
        Trainer trainer = new Trainer();
        trainer.setUsername("trainerUser");
        trainer.setPassword("securePass");

        CredentialsDTO credentialsDTO = trainerMapper.toCredentialsDTO(trainer);

        assertEquals("trainerUser", credentialsDTO.getUsername());
        assertEquals("securePass", credentialsDTO.getPassword());
    }
}

