package com.example.Inmopro.v1.Controller.Geography;

import com.example.Inmopro.v1.Model.Geography.Address;
import com.example.Inmopro.v1.Service.Geography.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> Address = addressService.findAll();
        if (Address.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(Address);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        return addressService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address savedAddress = addressService.save(address);
        URI location = URI.create("/addresses/" + savedAddress.getId());
        return ResponseEntity.created(location).body(savedAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        if (!addressService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
