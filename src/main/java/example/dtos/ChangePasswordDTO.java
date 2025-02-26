package example.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @NotNull
    @Size(min = 6, max = 20, message = "Password must be from 6 to 20 characters")
    private String oldPassword;

    @NotNull
    @Size(min = 6, max = 20, message = "Password must be from 6 to 20 characters")
    private String newPassword;
}
