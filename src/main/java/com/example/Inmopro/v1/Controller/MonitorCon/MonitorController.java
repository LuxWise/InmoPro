package com.example.Inmopro.v1.Controller.MonitorCon;

import com.example.Inmopro.v1.Controller.Request.RequestResponse;
import com.example.Inmopro.v1.Controller.Request.ThrowingSupplier;
import com.example.Inmopro.v1.Dto.Request.RequestMonitor;
import com.example.Inmopro.v1.Service.MonitorSer.MonitorService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Autowired
    private MonitorService monitorService;

    @GetMapping("/requests")
    //@PreAuthorize("hasRole('MONITOR')")
    public Response getSolicitudes(HttpServletRequest httpRequest) {
        return monitorService.getAllRequestsByRol(httpRequest);
    }
    @GetMapping("/requests/{requestId}")
    public Response getRequestById(@PathVariable Integer requestId, HttpServletRequest httpRequest) {
        return monitorService.getRequestById(requestId, httpRequest);
    }

    @PostMapping("create")
    public ResponseEntity<RequestResponse> createRequest(@RequestBody RequestMonitor request, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> monitorService.create(request, httpRequest));
    }
    @GetMapping("/request/{statusRequestId}")
    public ResponseEntity<Object[]> getAllRequestsByRolAndPending(@PathVariable Integer statusRequestId, HttpServletRequest httpRequest) {
        Optional<Object[]> requests = monitorService.getAllRequestsByRolAndPending(statusRequestId, httpRequest);
        return requests.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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