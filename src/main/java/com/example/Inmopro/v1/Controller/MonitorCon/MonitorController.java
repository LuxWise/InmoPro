package com.example.Inmopro.v1.Controller.MonitorCon;

import com.example.Inmopro.v1.Service.MonitorSer.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}