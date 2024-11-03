package com.example.Inmopro.v1.Controller.MonitorCon;

import com.example.Inmopro.v1.Controller.Request.RequestResponse;
import com.example.Inmopro.v1.Controller.Request.ThrowingSupplier;
import com.example.Inmopro.v1.Dto.Request.RequestMonitor;
import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Service.MonitorSer.MonitorService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Autowired
    private MonitorService monitorService;

    @GetMapping("/requests/{monitorId}")
    //@PreAuthorize("hasRole('MONITOR')")
    public ResponseEntity<Object[]> getSolicitudes(@PathVariable Integer monitorId) {
        Optional<Object[]> requests = monitorService.getAllRequestsByRol(monitorId);
        return requests.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/requests/{requestId}/monitor/{monitorId}")
    //@PreAuthorize("hasRole('MONITOR')")
    public ResponseEntity<Object[]> getRequestById(@PathVariable Integer requestId, @PathVariable Integer monitorId) {
        Optional<Object[]> requests = monitorService.getRequestByIdAndMonitorId(requestId, monitorId);
        return requests.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("create")
    public ResponseEntity<RequestResponse> createRequest(@RequestBody RequestMonitor request, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> monitorService.create(request, httpRequest));
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