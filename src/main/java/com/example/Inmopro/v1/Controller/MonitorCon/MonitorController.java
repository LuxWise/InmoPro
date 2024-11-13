package com.example.Inmopro.v1.Controller.MonitorCon;

import com.example.Inmopro.v1.Controller.Request.RequestResponse;
import com.example.Inmopro.v1.Controller.Request.ThrowingSupplier;
import com.example.Inmopro.v1.Dto.Request.RequestMonitor;
import com.example.Inmopro.v1.Service.Monitor.MonitorService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/monitor/request")
public class MonitorController {
    @Autowired
    private MonitorService monitorService;

    @GetMapping()
    public Response getSolicitudes(HttpServletRequest httpRequest) {
        return monitorService.getAllRequestsByRol(httpRequest);
    }
    @GetMapping("/{requestId}")
    public Response getRequestById(@PathVariable Integer requestId, HttpServletRequest httpRequest) {
        return monitorService.getRequestById(requestId, httpRequest);
    }
    @PostMapping()
    public ResponseEntity<RequestResponse> createRequest(@RequestBody RequestMonitor request, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> monitorService.create(request, httpRequest));
    }
    @GetMapping("/statusPending/{statusRequestId}")
    public Response getAllRequestsByRolAndPending(@PathVariable Integer statusRequestId, HttpServletRequest httpRequest) {
        return monitorService.getAllRequestsByRolAndPending(statusRequestId, httpRequest);
    }
    @PatchMapping("{requestId}/process")
    public ResponseEntity<RequestResponse> processRequest(@PathVariable Integer requestId,HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> monitorService.process(requestId, httpRequest));
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