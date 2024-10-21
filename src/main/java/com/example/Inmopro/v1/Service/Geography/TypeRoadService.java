package com.example.Inmopro.v1.Service.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.TypeRoad;
import com.example.Inmopro.v1.Repository.Geography.TypeRoadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Inmopro.v1.Exception.TypeRoadNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeRoadService {

    private final TypeRoadRepository typeRoadRepository;

    public List<TypeRoad> findAll() {
        return typeRoadRepository.findAll();
    }

    public Optional<TypeRoad> findById(Long id) {
        return typeRoadRepository.findById(id);
    }

}
