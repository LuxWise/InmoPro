package com.example.Inmopro.v1.Controller.Request;

import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Service.Request.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Obtener todas las solicitudes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitudes encontradas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Object.class)))),
    })
    @GetMapping()
    public ResponseEntity<Object[]> getAllRequests() {
        Optional<Object[]> requests = requestService.getAllRequests();
        return requests.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(summary = "Obtener solicitud por ID")
    @Parameter(name = "requestId", description = "ID de la solicitud", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Object.class)))),
    })
    @GetMapping("{requestId}")
    public ResponseEntity<Object[]> getRequestById(@PathVariable Integer requestId) {
        Optional<Object[]> foundRequest = requestService.getRequestById(requestId);
        return foundRequest.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva solicitud")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud creada",
                    content = @Content(schema = @Schema(implementation = RequestResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error al procesar la solicitud")
    })
    @PostMapping()
    public ResponseEntity<RequestResponse> createRequest(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la solicitud a crear", required = true,
                    content = @Content(schema = @Schema(implementation = RequestRequest.class)))
            @RequestBody RequestRequest request, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> requestService.create(request, httpRequest));
    }

    @Operation(summary = "Procesar una solicitud")
    @Parameter(name = "requestId", description = "ID de la solicitud", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud procesada",
                    content = @Content(schema = @Schema(implementation = RequestResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error al procesar la solicitud")
    })
    @PatchMapping("{requestId}/process")
    public ResponseEntity<RequestResponse> processRequest(@PathVariable Integer requestId, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> requestService.process(requestId, httpRequest));
    }

    @Operation(summary = "Aprobar una solicitud")
    @Parameter(name = "requestId", description = "ID de la solicitud", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud aprobada",
                    content = @Content(schema = @Schema(implementation = RequestResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error al aprobar la solicitud")
    })
    @PatchMapping("{requestId}/approve")
    public ResponseEntity<RequestResponse> approveRequest(@PathVariable Integer requestId, HttpServletRequest httpRequest) {
        return handleRequestProcess(() -> requestService.approve(requestId, httpRequest));
    }

    @Operation(summary = "Cancelar una solicitud")
    @Parameter(name = "requestId", description = "ID de la solicitud", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud cancelada",
                    content = @Content(schema = @Schema(implementation = RequestResponse.class)))
    })
    @PatchMapping("{requestId}/cancel")
    public ResponseEntity<RequestResponse> cancel(@PathVariable Integer requestId) {
        return ResponseEntity.ok(requestService.cancel(requestId));
    }

    @Operation(summary = "Poner una solicitud en espera")
    @Parameter(name = "requestId", description = "ID de la solicitud", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud en espera",
                    content = @Content(schema = @Schema(implementation = RequestResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error al procesar la solicitud")
    })
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
