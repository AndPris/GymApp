package example.controllers;

import example.dtos.ChangePasswordDTO;
import example.security.annotations.Authenticated;
import example.security.annotations.Authorized;
import example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Authenticated
    @Authorized
    @PutMapping("/{username}/password")
    public ResponseEntity<?> changeUserPassword(@PathVariable("username") String username,
                                                @RequestBody ChangePasswordDTO changePasswordDTO,
                                                @RequestHeader(value = "Authorization") String authHeader) {
        String oldPassword = changePasswordDTO.getOldPassword();

        if (!userService.existsUserByUsernameAndPassword(username, oldPassword)) {
            return ResponseEntity.notFound().build();
        }

        String newPassword = changePasswordDTO.getNewPassword();
        userService.changeUserPassword(username, oldPassword, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
