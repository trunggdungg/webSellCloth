package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
    List<Ward> findByDistrictId(Integer districtId);

   
}
