package com.example.Inmopro.v1.Service;

import com.example.Inmopro.v1.Model.Geography.Zone;
import com.example.Inmopro.v1.Repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }
}
