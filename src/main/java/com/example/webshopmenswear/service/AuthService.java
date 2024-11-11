package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.User;
import com.example.webshopmenswear.model.request.LoginRequest;
import com.example.webshopmenswear.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final HttpSession httpSession;

    public void login(LoginRequest loginRequest) {
        User user = (User) userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        httpSession.setAttribute("CURRENT_USER", user);

    }

    public void logout() {
        httpSession.removeAttribute("CURRENT_USER");
    }
}
