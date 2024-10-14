package com.example.Inmopro.v1.Service.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.QuadrantRoadGen;
import com.example.Inmopro.v1.Repository.Geography.QuadrantRoadGenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuadrantRoadGenService {
    @Autowired
    private QuadrantRoadGenRepository quadrantRoadGenRepository;

    public List<QuadrantRoadGen> findAll() {
        return quadrantRoadGenRepository.findAll();
    }

    public QuadrantRoadGen findById(Long id) {
        return quadrantRoadGenRepository.findById(id).orElse(null);
    }

    public QuadrantRoadGen save(QuadrantRoadGen quadrantRoadGen) {
        return quadrantRoadGenRepository.save(quadrantRoadGen);
    }

    public void delete(Long id) {
        quadrantRoadGenRepository.deleteById(id);
    }
}

