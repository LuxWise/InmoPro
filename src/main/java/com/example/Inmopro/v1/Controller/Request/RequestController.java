package com.example.Inmopro.v1.Controller.Request;

import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Service.Request.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService RequestService;

    @PostMapping("create")
    public ResponseEntity<RequestResponse> create(@RequestBody RequestRequest request, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(RequestService.create(request, httpRequest));
    }

}
