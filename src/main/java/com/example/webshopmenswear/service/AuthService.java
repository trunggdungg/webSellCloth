package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.User;
import com.example.webshopmenswear.model.Enum.UserRole;
import com.example.webshopmenswear.model.request.LoginRequest;
import com.example.webshopmenswear.model.request.ProfileRequest;
import com.example.webshopmenswear.model.request.SignUpRequest;
import com.example.webshopmenswear.model.request.UpSertUserRequest;
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

    public Object getUserById(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(Integer id, UpSertUserRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhone());
        user.setUserRole(UserRole.valueOf(request.getRole()));
        user.setIsActive(request.getIsActive());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User createUser(UpSertUserRequest request) {
        // Kiểm tra nếu email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại trong hệ thống.");
        }
        User user = User.builder()
            .email(request.getEmail())
            .password(bCryptPasswordEncoder.encode(request.getPassword()))
            .fullName(request.getFullName())
            .username(request.getUsername())
            .phoneNumber(request.getPhone())
            .avatar("/assets/images/avata_img.jpg")
            .userRole(UserRole.valueOf(request.getRole()))
            .isActive(request.getIsActive())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        return userRepository.save(user);
    }

    public void updateProfile(ProfileRequest profileRequest) {
        User user = getCurrentUser();
        user.setEmail(profileRequest.getEmail());
        user.setFullName(profileRequest.getFullName());
        user.setUsername(profileRequest.getUsername());
        user.setPhoneNumber(profileRequest.getPhoneNumber());
        userRepository.save(user);
    }
}
