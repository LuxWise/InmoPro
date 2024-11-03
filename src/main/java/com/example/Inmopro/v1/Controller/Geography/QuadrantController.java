package com.example.Inmopro.v1.Controller.Geography;


import com.example.Inmopro.v1.Model.Geography.QuadrantZone.Quadrant;
import com.example.Inmopro.v1.Service.Geography.QuadrantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/quadrants")
@RequiredArgsConstructor
public class QuadrantController {
    private final QuadrantService quadrantService;

    @GetMapping
    public ResponseEntity<List<Quadrant>> getAllQuadrants() {
        List<Quadrant> quadrants = quadrantService.findAll();
        if (quadrants.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(quadrants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quadrant> getQuadrantById(@PathVariable Long id) {
        return quadrantService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

