package com.example.Inmopro.v1.Controller.Geography;


import com.example.Inmopro.v1.Model.Geography.QuadrantZone.TypeRoad;
import com.example.Inmopro.v1.Model.Geography.Zone;
import com.example.Inmopro.v1.Repository.Geography.TypeRoadRepository;
import com.example.Inmopro.v1.Service.Geography.TypeRoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/typeroads")
public class TypeRoadController {

    @Autowired
    private TypeRoadRepository typeRoadRepository;

    //@GetMapping
    //public List<TypeRoad> getAllTypeRoads() {
    //   return typeRoadRepository.findAll();
    //}

    @GetMapping
    public List<TypeRoad> getAllTypeRoads() {
        List<TypeRoad> roads = typeRoadRepository.findAll();
        System.out.println("Número de TypeRoads encontrados: " + roads.size());
        return roads;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TypeRoad> getTypeRoadById(@PathVariable Long id) {
        return typeRoadRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TypeRoad createTypeRoad(@RequestBody TypeRoad typeRoad) {
        return typeRoadRepository.save(typeRoad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeRoad> updateTypeRoad(@PathVariable Long id, @RequestBody TypeRoad typeRoadDetails) {
        return typeRoadRepository.findById(id)
                .map(typeRoad -> {
                    typeRoad.setTypeRoad(typeRoadDetails.getTypeRoad());
                    typeRoad.setVisible(typeRoadDetails.getVisible());
                    return ResponseEntity.ok(typeRoadRepository.save(typeRoad));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    /*
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeRoad(@PathVariable Long id) {
        return typeRoadRepository.findById(id)
                .map(typeRoad -> {
                    typeRoadRepository.delete(typeRoad);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    */
}