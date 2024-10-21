package com.example.Inmopro.v1.Controller.Auth;

import com.example.Inmopro.v1.Dto.Auth.LoginRequest;
import com.example.Inmopro.v1.Service.Auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return handleLogin(request);
    }

    private ResponseEntity<AuthResponse> handleLogin(LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(AuthResponse.builder().message("Invalid credentials provided").build());
        } catch (LockedException e) {
            return ResponseEntity.status(423).body(AuthResponse.builder().message("Account is locked. Please contact support.").build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(AuthResponse.builder().message("Internal server error").build());
        }
    }

}
