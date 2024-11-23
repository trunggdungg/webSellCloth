package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Ward;
import com.example.webshopmenswear.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class wardService {
    private final WardRepository wardRepository;

    public List<Ward> getWardByDistrictId(Integer DistrictId) {
        return wardRepository.findByDistrictId(DistrictId);
    }

    public List<Ward> getWardByDistrict(Integer districtId) {
        return wardRepository.findByDistrictId(districtId);
    }
}
