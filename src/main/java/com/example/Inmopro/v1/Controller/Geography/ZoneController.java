package com.example.Inmopro.v1.Controller.Geography;

import com.example.Inmopro.v1.Model.Geography.Zone;
import com.example.Inmopro.v1.Service.Geography.ZoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/zone")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @Operation(summary = "Obtener todas las zonas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zonas encontradas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Zone.class)))),
    })
    @GetMapping()
    public List<Zone> getZones() {
        return zoneService.getAllZones();
    }
}
