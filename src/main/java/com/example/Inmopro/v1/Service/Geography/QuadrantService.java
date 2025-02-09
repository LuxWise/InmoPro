package com.example.Inmopro.v1.Service.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.Quadrant;
import com.example.Inmopro.v1.Repository.Geography.QuadrantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuadrantService {
    private final QuadrantRepository quadrantRepository;

    public List<Quadrant> findAll() {
        return quadrantRepository.findAll();
    }

    public Optional<Quadrant> findById(Long id) {
        return quadrantRepository.findById(id);
    }
}

