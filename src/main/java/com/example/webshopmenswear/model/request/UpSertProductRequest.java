package com.example.webshopmenswear.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpSertProductRequest {
    String name;
    String description;
    Double price;
    String material;
    Integer categoryId;
    Boolean status;
    Integer discount;
}
