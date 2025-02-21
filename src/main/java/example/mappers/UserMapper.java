package example.mappers;

import example.dtos.CredentialsDTO;
import example.entities.User;

public abstract class UserMapper {
    public CredentialsDTO toCredentialsDTO(User user) {
        CredentialsDTO credentialsDTO = new CredentialsDTO();

        credentialsDTO.setUsername(user.getUsername());
        credentialsDTO.setPassword(user.getPassword());

        return credentialsDTO;
    }
}
