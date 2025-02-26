package example.controllers;

import example.dtos.ChangePasswordDTO;
import example.security.annotations.Authenticated;
import example.security.annotations.Authorized;
import example.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Authorization failed",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No user with such username",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid request body",
                    content = @Content)
    })
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
