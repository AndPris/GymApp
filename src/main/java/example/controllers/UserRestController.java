package example.controllers;

import example.dtos.ChangePasswordDTO;
import example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/password")
    public ResponseEntity<?> changeUserPassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        String username = changePasswordDTO.getUsername();
        String oldPassword = changePasswordDTO.getOldPassword();

        if (!userService.existsUserByUsernameAndPassword(username, oldPassword)) {
            return ResponseEntity.notFound().build();
        }

        String newPassword = changePasswordDTO.getNewPassword();
        userService.changeUserPassword(username, oldPassword, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
