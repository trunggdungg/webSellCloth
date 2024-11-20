package com.example.webshopmenswear.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    String username;
    String password;
    String email;
    String phone;
    String address;
    String fullName;
    String avatar;
    String role;
    String status;
}
