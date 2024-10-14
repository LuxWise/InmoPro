package com.example.Inmopro.v1.Controller.Geography;


import com.example.Inmopro.v1.Model.Geography.QuadrantZone.Quadrant;
import com.example.Inmopro.v1.Service.Geography.QuadrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quadrants")
public class QuadrantController {
    @Autowired
    private QuadrantService quadrantService;

    @GetMapping
    public List<Quadrant> getAllQuadrants() {
        return quadrantService.findAll();
    }

    @GetMapping("/{id}")
    public Quadrant getQuadrantById(@PathVariable Long id) {
        return quadrantService.findById(id);
    }

    @PostMapping
    public Quadrant createQuadrant(@RequestBody Quadrant quadrant) {
        return quadrantService.save(quadrant);
    }

    @DeleteMapping("/{id}")
    public void deleteQuadrant(@PathVariable Long id) {
        quadrantService.delete(id);
    }
}

