package example.controllers;

import example.dtos.ChangePasswordDTO;
import example.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserRestControllerTests {

    @InjectMocks
    private UserRestController userRestController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void changeUserPassword_ShouldReturnOk() {
        String username = "test";
        String oldPassword = "oldPass";
        String newPassword = "newPass";
        String authHeader = "test";

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword(oldPassword);
        changePasswordDTO.setNewPassword(newPassword);

        when(userService.existsUserByUsernameAndPassword(username, oldPassword)).thenReturn(true);

        ResponseEntity<?> response = userRestController.changeUserPassword(username, changePasswordDTO, authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void changeUserPassword_ShouldReturnNotFound() {
        String username = "test";
        String oldPassword = "wrongPass";
        String newPassword = "newPass";
        String authHeader = "test";

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword(oldPassword);
        changePasswordDTO.setNewPassword(newPassword);

        when(userService.existsUserByUsernameAndPassword(username, oldPassword)).thenReturn(false);

        ResponseEntity<?> response = userRestController.changeUserPassword(username, changePasswordDTO, authHeader);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, never()).changeUserPassword(anyString(), anyString(), anyString());
    }
}
