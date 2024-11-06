package com.example.Inmopro.v1.Controller.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.QuadrantRoadGen;
import com.example.Inmopro.v1.Service.Geography.QuadrantRoadGenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quadrant-road-gen")
@RequiredArgsConstructor
public class QuadrantRoadGenController {

    private final QuadrantRoadGenService quadrantRoadGenService;

    @GetMapping
    public ResponseEntity<List<QuadrantRoadGen>> getAllQuadrantRoadGen() {
        List<QuadrantRoadGen> quadrantRoadGens = quadrantRoadGenService.findAll();
        if (quadrantRoadGens.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(quadrantRoadGens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuadrantRoadGen> getQuadrantRoadGenById(@PathVariable Long id) {
        return quadrantRoadGenService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}