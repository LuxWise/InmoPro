package com.example.Inmopro.v1.Controller.Request;

import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import com.example.Inmopro.v1.Service.Request.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/followup-requests")
@RequiredArgsConstructor
public class FollowUpRequestController {

    private final RequestService requestService;

    @Operation(summary = "Obtener todas las solicitudes de seguimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitudes de seguimiento encontradas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FollowUpRequest.class)))),
    })
    @GetMapping()
    public ResponseEntity<List<FollowUpRequest>> getAllFollowUpRequests() {
        return ResponseEntity.ok(requestService.getFollowUpRequest());
    }

    @Operation(summary = "Obtener solicitudes de seguimiento por estado")
    @Parameter(name = "statusName", description = "Nombre del estado de la solicitud", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitudes de seguimiento encontradas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Object.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron solicitudes de seguimiento con el estado proporcionado")
    })
    @GetMapping("/status/{statusName}")
    public ResponseEntity<List<Object[]>> getFollowUpRequestsByStatusName(@PathVariable String statusName) {
        List<Object[]> followUpRequests = requestService.getFollowUpRequestsByStatusName(statusName);
        if (followUpRequests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(followUpRequests);
    }
}
