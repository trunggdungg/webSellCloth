package com.example.webshopmenswear.rest;

import com.example.webshopmenswear.model.request.LoginRequest;
import com.example.webshopmenswear.model.request.ProfileRequest;
import com.example.webshopmenswear.model.request.SignUpRequest;
import com.example.webshopmenswear.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        authService.login(loginRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileRequest profileRequest) {
        authService.updateProfile(profileRequest);
        return ResponseEntity.ok().build();
    }

}
