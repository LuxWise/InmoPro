package com.example.Inmopro.v1.Controller.Request;

import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Service.Request.RequestService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping()
    public ResponseEntity<Object[]> getAllRequests() {
        Optional<Object[]> requests = requestService.getAllRequests();
        return requests.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object[]> getRequestById(@PathVariable Integer requestId) {
        Optional<Object[]> foundRequest = requestService.getRequestById(requestId);
        return foundRequest.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<RequestResponse> createRequest(@RequestBody RequestRequest request, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> requestService.create(request, httpRequest));
    }

    @PatchMapping("{requestId}/process")
    public ResponseEntity<RequestResponse> processRequest(@PathVariable Integer requestId,HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> requestService.process(requestId, httpRequest));
    }

    @PatchMapping("{requestId}/approve")
    public ResponseEntity<RequestResponse> approveRequest(@PathVariable Integer requestId, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> requestService.approve(requestId, httpRequest));
    }


    @PatchMapping("{requestId}/cancel")
    public ResponseEntity<RequestResponse> cancel(@PathVariable Integer requestId) {
        return ResponseEntity.ok(requestService.cancel(requestId));
    }

    @PatchMapping("{requestId}/onHold")
    public ResponseEntity<RequestResponse> onHold(@PathVariable Integer requestId, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> requestService.onHold(requestId, httpRequest));
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
