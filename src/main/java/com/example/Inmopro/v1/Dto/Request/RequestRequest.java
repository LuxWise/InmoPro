package com.example.Inmopro.v1.Dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para crear o actualizar una solicitud")
public class RequestRequest {

    @Schema(description = "Tipo de solicitud", example = "1")
    private Integer requestType;

    @Schema(description = "Descripción de la solicitud", example = "Inconvenientes con el servicio de agua")
    private String description;

    public Integer getRequestType() {
        return requestType;
    }

    public String getDescription() {
        return description;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

