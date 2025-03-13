package example.controllers;

import example.dtos.CredentialsDTO;
import example.entities.User;
import example.exceptions.IPAddressBlockedException;
import example.security.LoginAttemptService;
import example.security.jwt.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private final LoginAttemptService loginAttemptService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public LoginController(LoginAttemptService loginAttemptService,
                           AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.loginAttemptService = loginAttemptService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "45gdf.234g.56hfh2"))),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Invalid username and password"))),
            @ApiResponse(responseCode = "503", description = "IP address temporary blocked",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Try to login later, please")))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsDTO credentialsDTO) {
        if(loginAttemptService.isBlocked()) {
            throw new IPAddressBlockedException("Try to login later, please");
        }

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            credentialsDTO.getUsername(), credentialsDTO.getPassword()));

            User user = (User) authentication.getPrincipal();
            String token = jwtTokenUtil.generateAccessToken(user, 1000 * 60);

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            token
                    )
                    .body(token);
        } catch (BadCredentialsException exception) {
            loginAttemptService.loginFailed();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");
        }
    }
}
