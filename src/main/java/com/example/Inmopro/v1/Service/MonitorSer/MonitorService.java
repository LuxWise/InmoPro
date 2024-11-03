package com.example.Inmopro.v1.Service.MonitorSer;

import com.example.Inmopro.v1.Repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonitorService {
    @Autowired
    private  RequestRepository requestRepository;


    public Optional<Object[]> getAllRequestsByRol(Integer monitorId) {
        return requestRepository.findAllRequestsByRol(monitorId);
    }
}
