package com.example.Inmopro.v1.Controller;

import com.example.Inmopro.v1.Model.Geography.Address;
import com.example.Inmopro.v1.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.findAll();
    }

    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Long id) {
        return addressService.findById(id);
    }

    @PostMapping
    public Address createAddress(@RequestBody Address address) {
        return addressService.save(address);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.delete(id);
    }
}
