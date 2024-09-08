package com.example.Inmopro.v1.Dto.Request;

public class RequestRequest {

    private Integer requestType;
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
