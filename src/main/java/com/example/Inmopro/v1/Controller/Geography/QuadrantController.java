package com.example.Inmopro.v1.Controller.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.Quadrant;
import com.example.Inmopro.v1.Service.Geography.QuadrantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quadrants")
@RequiredArgsConstructor
public class QuadrantController {

    private final QuadrantService quadrantService;

    @Operation(summary = "Obtener todas las zonas cuadrantes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zonas cuadrantes encontradas",
                    content = @Content(array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @Schema(implementation = Quadrant.class)))),
    })
    @GetMapping
    public ResponseEntity<List<Quadrant>> getAllQuadrants() {
        List<Quadrant> quadrants = quadrantService.findAll();
        if (quadrants.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(quadrants);
    }

    @Operation(summary = "Obtener zona cuadrante por ID")
    @Parameter(name = "id", description = "ID de la zona cuadrante", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zona cuadrante encontrada",
                    content = @Content(schema = @Schema(implementation = Quadrant.class))),
            @ApiResponse(responseCode = "404", description = "Zona cuadrante no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Quadrant> getQuadrantById(@PathVariable Long id) {
        return quadrantService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

