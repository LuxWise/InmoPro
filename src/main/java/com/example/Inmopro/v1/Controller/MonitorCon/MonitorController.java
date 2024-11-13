package com.example.Inmopro.v1.Controller.MonitorCon;

import com.example.Inmopro.v1.Controller.Request.RequestResponse;
import com.example.Inmopro.v1.Controller.Request.ThrowingSupplier;
import com.example.Inmopro.v1.Dto.Request.RequestMonitor;
import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Service.Monitor.MonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitorController {
    private final MonitorService monitorService;

    @Operation(summary = "Obtener todas las solicitudes según el rol del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitudes encontradas",
                    content = @Content(schema = @Schema(implementation = RequestRequest.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron solicitudes")
    })
    @GetMapping("/requests")
    public MonitorResponse getSolicitudes(HttpServletRequest httpRequest) {
        return monitorService.getAllRequestsByRol(httpRequest);
    }

    @Operation(summary = "Obtener solicitud específica por ID y rol del usuario")
    @Parameter(name = "requestId", description = "ID de la solicitud", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada",
                    content = @Content(schema = @Schema(implementation = RequestRequest.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @GetMapping("/requests/{requestId}")
    public MonitorResponse getRequestById(@PathVariable Integer requestId, HttpServletRequest httpRequest) {
        return monitorService.getRequestById(requestId, httpRequest);
    }

    @Operation(summary = "Crear una nueva solicitud")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud creada",
                    content = @Content(schema = @Schema(implementation = RequestRequest.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("create")
    public ResponseEntity<RequestResponse> createRequest(@RequestBody RequestMonitor request, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> monitorService.create(request, httpRequest));
    }

    @Operation(summary = "Obtener todas las solicitudes pendientes según el rol y estado de solicitud")
    @Parameter(name = "statusRequestId", description = "ID del estado de la solicitud pendiente", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitudes encontradas",
                    content = @Content(schema = @Schema(implementation = RequestRequest.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron solicitudes pendientes con el estado especificado")
    })
    @GetMapping("/request/statusPending/{statusRequestId}")
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