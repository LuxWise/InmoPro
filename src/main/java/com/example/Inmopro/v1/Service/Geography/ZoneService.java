package com.example.Inmopro.v1.Service.Geography;

import com.example.Inmopro.v1.Model.Geography.Zone;
import com.example.Inmopro.v1.Repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }
}
