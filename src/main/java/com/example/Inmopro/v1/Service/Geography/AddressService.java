package com.example.Inmopro.v1.Service.Geography;

import com.example.Inmopro.v1.Model.Geography.Address;
import com.example.Inmopro.v1.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Address findById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public void delete(Long id) {
        addressRepository.deleteById(id);
    }
}
