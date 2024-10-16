package com.example.Inmopro.v1.Controller.Request;

import com.example.Inmopro.v1.Dto.Request.RequestApprove;
import com.example.Inmopro.v1.Dto.Request.RequestCancel;
import com.example.Inmopro.v1.Dto.Request.RequestProcess;
import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import com.example.Inmopro.v1.Service.Request.RequestService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PatchExchange;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping("requests")
    public ResponseEntity<Optional<Object[]>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    @GetMapping("requests/{requestId}")
    public ResponseEntity<Object[]> getRequestById(@PathVariable Integer requestId) {
        Optional<Object[]> request = requestService.getRequestById(requestId);
        return request.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("create")
    public ResponseEntity<RequestResponse> create(@RequestBody RequestRequest request, HttpServletRequest httpRequest) {
        try {
            return ResponseEntity.ok(requestService.create(request, httpRequest));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(RequestResponse.builder().message("Error to read file").build());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(RequestResponse.builder().message("Error to send email").build());
        }
    }

    @PatchMapping("process")
    public ResponseEntity<RequestResponse> process(@RequestBody RequestProcess request,HttpServletRequest httpRequest) {
        try {
            return ResponseEntity.ok(requestService.process(request, httpRequest));
        } catch (IOException e){
            return ResponseEntity.status(500).body(RequestResponse.builder().message("Error to read file").build());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(RequestResponse.builder().message("Error to send email").build());
        }
    }

    @PatchMapping("approve")
    public ResponseEntity<RequestResponse> process(@RequestBody RequestApprove request,HttpServletRequest httpRequest) {
        try {
            return ResponseEntity.ok(requestService.approve(request, httpRequest));
        } catch (IOException e){
            return ResponseEntity.status(500).body(RequestResponse.builder().message("Error to read file").build());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(RequestResponse.builder().message("Error to send email").build());
        }
    }

    @PatchMapping("cancel")
    public ResponseEntity<RequestResponse> cancel(@RequestBody RequestCancel request) {
        return ResponseEntity.ok(requestService.cancel(request));
    }

    @GetMapping("followuprequests")
    public ResponseEntity<List<FollowUpRequest>> getAllFollowUpRequests() {
        return ResponseEntity.ok(requestService.getFollowUpRequest());
    }

    @GetMapping("/followuprequests/{statusName}")
    public ResponseEntity<List<Object[]>> getFollowUpRequestsByStatusName(@PathVariable String statusName) {
        List<Object[]> followUpRequests = requestService.getFollowUpRequestsByStatusName(statusName);
        if (followUpRequests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(followUpRequests);
    }

}
