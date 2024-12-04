package com.example.webshopmenswear.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpSertUserRequest {
    String email;
    String fullName;
    String username;
    String password;
    String phone;
    String role;
    Boolean isActive;
}
