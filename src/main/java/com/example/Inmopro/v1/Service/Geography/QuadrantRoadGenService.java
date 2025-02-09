package com.example.Inmopro.v1.Service.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.QuadrantRoadGen;
import com.example.Inmopro.v1.Repository.Geography.QuadrantRoadGenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuadrantRoadGenService {

    private final QuadrantRoadGenRepository quadrantRoadGenRepository;

    public List<QuadrantRoadGen> findAll() {
        return quadrantRoadGenRepository.findAll();
    }

    public Optional<QuadrantRoadGen> findById(Long id) {
        return quadrantRoadGenRepository.findById(id);
    }

}

