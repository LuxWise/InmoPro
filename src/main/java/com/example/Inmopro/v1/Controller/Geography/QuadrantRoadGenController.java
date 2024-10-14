package com.example.Inmopro.v1.Controller.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.QuadrantRoadGen;
import com.example.Inmopro.v1.Service.Geography.QuadrantRoadGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quadrant-road-gen")
public class QuadrantRoadGenController {
    @Autowired
    private QuadrantRoadGenService quadrantRoadGenService;

    @GetMapping
    public List<QuadrantRoadGen> getAllQuadrantRoadGen() {
        return quadrantRoadGenService.findAll();
    }

    @GetMapping("/{id}")
    public QuadrantRoadGen getQuadrantRoadGenById(@PathVariable Long id) {
        return quadrantRoadGenService.findById(id);
    }

    @PostMapping
    public QuadrantRoadGen createQuadrantRoadGen(@RequestBody QuadrantRoadGen quadrantRoadGen) {
        return quadrantRoadGenService.save(quadrantRoadGen);
    }

    @DeleteMapping("/{id}")
    public void deleteQuadrantRoadGen(@PathVariable Long id) {
        quadrantRoadGenService.delete(id);
    }
}