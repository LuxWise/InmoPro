package com.example.Inmopro.v1.Controller.MonitorCon;

import com.example.Inmopro.v1.Service.MonitorSer.MonitorService;
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

    @GetMapping("/solicitudes")
    //@PreAuthorize("hasRole('MONITOR')")
    public ResponseEntity<Object[]> getSolicitudes() {
        Optional<Object[]> requests = MonitorService.getAllRequestsByRol();
        // Lógica para obtener solicitudes
        return requests.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
    /*
    @GetMapping("requests/{requestId}")
    public ResponseEntity<Object[]> getRequestById(@PathVariable Integer requestId) {
        Optional<Object[]> foundRequest = requestService.getRequestById(requestId);
        return foundRequest.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
*/
    // Otros métodos protegidos para el monitor
}