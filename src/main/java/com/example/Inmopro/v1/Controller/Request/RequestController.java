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
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping("requests")
    public ResponseEntity<Object[]> getAllRequests() {
        Optional<Object[]> requests = requestService.getAllRequests();
        return requests.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("requests/{requestId}")
    public ResponseEntity<Object[]> getRequestById(@PathVariable Integer requestId) {
        Optional<Object[]> foundRequest = requestService.getRequestById(requestId);
        return foundRequest.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("create")
    public ResponseEntity<RequestResponse> createRequest(@RequestBody RequestRequest request, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> requestService.create(request, httpRequest));
    }

    @PatchMapping("process")
    public ResponseEntity<RequestResponse> processRequest(@RequestBody RequestProcess request,HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> requestService.process(request, httpRequest));
    }

    @PatchMapping("approve")
    public ResponseEntity<RequestResponse> approveRequest(@RequestBody RequestApprove request, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> requestService.approve(request, httpRequest));
    }


    @PatchMapping("cancel")
    public ResponseEntity<RequestResponse> cancel(@RequestBody RequestCancel request) {
        return ResponseEntity.ok(requestService.cancel(request));
    }

    @GetMapping("followuprequests")
    public ResponseEntity<List<FollowUpRequest>> getAllFollowUpRequests() {
        return ResponseEntity.ok(requestService.getFollowUpRequest());
    }

    @GetMapping("/followuprequests/status/{statusName}")
    public ResponseEntity<List<Object[]>> getFollowUpRequestsByStatusName(@PathVariable String statusName) {
        List<Object[]> followUpRequests = requestService.getFollowUpRequestsByStatusName(statusName);
        if (followUpRequests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(followUpRequests);
    }

    private ResponseEntity<RequestResponse> handleRequestProcess(ThrowingSupplier<RequestResponse> supplier) {
        try {
            return ResponseEntity.ok(supplier.get());
        } catch (IOException e) {
            return ResponseEntity.status(500).body(RequestResponse.builder().message("Error to read file").build());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(RequestResponse.builder().message("Error to send email").build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(RequestResponse.builder().message("Internal Server Error").build());
        }

    }
}
