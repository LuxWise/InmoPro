package com.example.Inmopro.v1.Controller.Geography;


import com.example.Inmopro.v1.Model.Geography.QuadrantZone.QuadrantRoadGen;
import com.example.Inmopro.v1.Model.Geography.QuadrantZone.TypeRoad;
import com.example.Inmopro.v1.Model.Geography.Zone;
import com.example.Inmopro.v1.Repository.Geography.TypeRoadRepository;
import com.example.Inmopro.v1.Service.Geography.TypeRoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/typeroads")
@RequiredArgsConstructor
public class TypeRoadController {

    private final TypeRoadService typeRoadService;

    @GetMapping
    public ResponseEntity<List<TypeRoad>> getAllTypeRoads() {
        List<TypeRoad> typeRoad = typeRoadService.findAll();
        if (typeRoad.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(typeRoad);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeRoad> getTypeRoadById(@PathVariable Long id) {
        return typeRoadService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



}