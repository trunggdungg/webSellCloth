package com.example.webshopmenswear.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetailRequest {
    private Integer productId;
    private Integer quantity;
    private Double price;

}
