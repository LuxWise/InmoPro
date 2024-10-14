package com.example.Inmopro.v1.Service.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.TypeRoad;
import com.example.Inmopro.v1.Repository.Geography.TypeRoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Inmopro.v1.Exception.TypeRoadNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class TypeRoadService {
    @Autowired
    private TypeRoadRepository typeRoadRepository;

    public List<TypeRoad> getAllTypeRoad() {
        return typeRoadRepository.findAll();
        //System.out.println("TypeRoads found: " + roads.size());
        //return roads;
    }
    /*public List<TypeRoad> findAll() {
        return typeRoadRepository.findAll();
    }*/

    public TypeRoad findById(Long id, Integer tup) {
        return typeRoadRepository.findById(id).orElse(null);
    }
    public TypeRoad findById(Long id) {
        Optional<TypeRoad> typeRoadOpt = typeRoadRepository.findById(id);
        return typeRoadOpt.orElseThrow(() ->
                new TypeRoadNotFoundException("TypeRoad not found with ID: " + id));
    }

    public TypeRoad save(TypeRoad typeRoad) {
        return typeRoadRepository.save(typeRoad);
    }

    public void delete(Long id) {
        typeRoadRepository.deleteById(id);
    }
}
