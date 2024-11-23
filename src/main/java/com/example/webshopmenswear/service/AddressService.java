package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Address;
import com.example.webshopmenswear.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void saveAddress(Address address) {
        addressRepository.save(address);
    }

    // Lấy tất cả địa chỉ của một người dùng
    public List<Address> getAddressesByUserId(Integer userId) {
        return addressRepository.findByUserId(userId);
    }

}
