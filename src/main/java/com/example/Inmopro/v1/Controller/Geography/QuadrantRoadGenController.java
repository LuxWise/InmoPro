package com.example.Inmopro.v1.Controller.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.QuadrantRoadGen;
import com.example.Inmopro.v1.Service.Geography.QuadrantRoadGenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quadrant-road-gen")
@RequiredArgsConstructor
public class QuadrantRoadGenController {

    private final QuadrantRoadGenService quadrantRoadGenService;

    @Operation(summary = "Obtener todas las cuadrantes de generación de caminos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuadrantes de generación de caminos encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuadrantRoadGen.class)))),
            @ApiResponse(responseCode = "204", description = "No hay cuadrantes de generación de caminos disponibles")
    })
    @GetMapping
    public ResponseEntity<List<QuadrantRoadGen>> getAllQuadrantRoadGen() {
        List<QuadrantRoadGen> quadrantRoadGens = quadrantRoadGenService.findAll();
        if (quadrantRoadGens.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(quadrantRoadGens);
    }

    @Operation(summary = "Obtener cuadrante de generación de camino por ID")
    @Parameter(name = "id", description = "ID del cuadrante de generación de camino", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuadrante de generación de camino encontrado",
                    content = @Content(schema = @Schema(implementation = QuadrantRoadGen.class))),
            @ApiResponse(responseCode = "404", description = "Cuadrante de generación de camino no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuadrantRoadGen> getQuadrantRoadGenById(@PathVariable Long id) {
        return quadrantRoadGenService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
