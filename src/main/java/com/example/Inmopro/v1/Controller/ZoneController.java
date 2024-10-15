package com.example.Inmopro.v1.Controller;

import com.example.Inmopro.v1.Model.Geography.Zone;
import com.example.Inmopro.v1.Service.Geography.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @GetMapping("/zones")
    public List<Zone> getZones() {
        return zoneService.getAllZones();
    }
}
