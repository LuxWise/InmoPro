package com.example.Inmopro.v1.Controller.Demov1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {
    @PostMapping(value = "create")
    public ResponseEntity<String> create(@RequestBody DemoRequest request) {
        return ResponseEntity.ok(DemoService.create(request));
    }
}
