package com.example.webshopmenswear.Specification;

import com.example.webshopmenswear.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> findProducts(String namecategory, String color, String size) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Nếu namecategory không null hoặc rỗng, thêm điều kiện vào predicates
            if (namecategory != null && !namecategory.isEmpty()) {
                predicates.add(cb.equal(root.get("category").get("name"), namecategory));
            }

            // Nếu color không null hoặc rỗng, thêm điều kiện vào predicates
            if (color != null && !color.isEmpty()) {
                predicates.add(cb.equal(root.join("colors").get("nameColor"), color));
            }

            // Nếu size không null hoặc rỗng, thêm điều kiện vào predicates
            if (size != null && !size.isEmpty()) {
                predicates.add(cb.equal(root.join("sizes").get("sizeName"), size));
            }

            // Trả về tất cả các predicate đã tạo với điều kiện AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
