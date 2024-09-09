package com.example.Inmopro.v1.Controller.Demov1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoService {
    public static String create(DemoRequest request) {
        return "El numero " + request.getNumber();
    }
}
