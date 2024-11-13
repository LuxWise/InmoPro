package com.example.Inmopro.v1.Controller.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.TypeRoad;
import com.example.Inmopro.v1.Service.Geography.TypeRoadService;
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
@RequestMapping("/api/v1/typeroads")
@RequiredArgsConstructor
public class TypeRoadController {

    private final TypeRoadService typeRoadService;

    @Operation(summary = "Obtener todos los tipos de carretera")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipos de carretera encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TypeRoad.class)))),
    })
    @GetMapping
    public ResponseEntity<List<TypeRoad>> getAllTypeRoads() {
        List<TypeRoad> typeRoads = typeRoadService.findAll();
        if (typeRoads.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(typeRoads);
    }

    @Operation(summary = "Obtener tipo de carretera por ID")
    @Parameter(name = "id", description = "ID del tipo de carretera", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de carretera encontrado",
                    content = @Content(schema = @Schema(implementation = TypeRoad.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de carretera no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TypeRoad> getTypeRoadById(@PathVariable Long id) {
        return typeRoadService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
