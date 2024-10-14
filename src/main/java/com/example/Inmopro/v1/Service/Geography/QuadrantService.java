package com.example.Inmopro.v1.Service.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.Quadrant;
import com.example.Inmopro.v1.Repository.Geography.QuadrantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuadrantService {
    @Autowired
    private QuadrantRepository quadrantRepository;

    public List<Quadrant> findAll() {
        return quadrantRepository.findAll();
    }

    public Quadrant findById(Long id) {
        return quadrantRepository.findById(id).orElse(null);
    }

    public Quadrant save(Quadrant quadrant) {
        return quadrantRepository.save(quadrant);
    }

    public void delete(Long id) {
        quadrantRepository.deleteById(id);
    }
}

