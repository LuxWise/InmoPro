package com.example.Inmopro.v1.Controller.Request;

import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import com.example.Inmopro.v1.Repository.FollowUpRequestRepository;
import com.example.Inmopro.v1.Service.Request.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService RequestService;
    private final FollowUpRequestRepository followUpRequestRepository;

    @PostMapping("create")
    public ResponseEntity<RequestResponse> create(@RequestBody RequestRequest request, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(RequestService.create(request, httpRequest));
    }

    @GetMapping("followuprequests")
    public ResponseEntity<List<FollowUpRequest>> getAllFollowUpRequests() {
        List<FollowUpRequest> followUpRequests = followUpRequestRepository.findAll();
        return ResponseEntity.ok(followUpRequests);
    }

}
