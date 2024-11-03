package com.example.Inmopro.v1.Service.MonitorSer;

import com.example.Inmopro.v1.Repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonitorService {
    private final RequestRepository requestRepository;


    public Optional<Object[]> getAllRequestsByRol() {
        return requestRepository.findAllRequestsByRol();
    }
}
