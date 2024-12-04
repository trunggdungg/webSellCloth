package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<Object> findByEmail(String email);

    boolean existsByEmail(String email);
}
