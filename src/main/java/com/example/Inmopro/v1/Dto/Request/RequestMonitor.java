package com.example.Inmopro.v1.Dto.Request;

import java.security.PrivateKey;

public class RequestMonitor {

    private Integer idUser;
    private Integer requestType;
    private String description;
    private String email;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getIdUser() {
        return idUser;
    }
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
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
