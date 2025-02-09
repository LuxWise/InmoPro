package com.example.Inmopro.v1.Service.Geography;

import com.example.Inmopro.v1.Model.Geography.Address;
import com.example.Inmopro.v1.Repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public boolean existsById(Long id) {
        return addressRepository.existsById(id);
    }

    public void delete(Long id) {
        addressRepository.deleteById(id);
    }
}
