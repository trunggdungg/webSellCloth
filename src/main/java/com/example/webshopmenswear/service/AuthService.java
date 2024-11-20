package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.User;
import com.example.webshopmenswear.model.Enum.UserRole;
import com.example.webshopmenswear.model.request.LoginRequest;
import com.example.webshopmenswear.model.request.SignUpRequest;
import com.example.webshopmenswear.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

        if (!user.getIsActive()) {
            throw new RuntimeException("User is not active");
        }

        httpSession.setAttribute("CURRENT_USER", user);
    }

    public void logout() {
        httpSession.removeAttribute("CURRENT_USER");
    }

    public User getCurrentUser() {
        User user = (User) httpSession.getAttribute("CURRENT_USER");
        if (user == null) {
            throw new RuntimeException("User is not logged in");
        }
        return user;
    }

    public void signUp(SignUpRequest signUpRequest) {
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            throw new RuntimeException("Password and confirm password do not match");
        }

        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
            .email(signUpRequest.getEmail())
            .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
            .fullName(signUpRequest.getFullName())
            .username(signUpRequest.getUsername())
            .phoneNumber(signUpRequest.getPhone())
            .avatar("static/assets/images/avata_img.jpg")
            .userRole(UserRole.USER)
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        userRepository.save(user);
    }

    public Object getAllUsers() {
        return userRepository.findAll();
    }
}
