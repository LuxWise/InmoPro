package com.example.Inmopro.v1.Controller.Geography;

import com.example.Inmopro.v1.Model.Geography.Zone;
import com.example.Inmopro.v1.Service.Geography.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/zone")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping()
    public List<Zone> getZones() {
        return zoneService.getAllZones();
    }
}
