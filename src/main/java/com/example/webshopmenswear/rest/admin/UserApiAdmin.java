package com.example.webshopmenswear.rest.admin;

import com.example.webshopmenswear.entity.User;
import com.example.webshopmenswear.model.request.UpSertUserRequest;
import com.example.webshopmenswear.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class UserApiAdmin {
    private final AuthService authService;

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UpSertUserRequest request) {
        User user = authService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UpSertUserRequest request) {
        User user = authService.createUser(request);
        return ResponseEntity.ok(user);
    }

}
