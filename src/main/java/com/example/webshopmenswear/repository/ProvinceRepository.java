package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    List<Province> findAll();
}
