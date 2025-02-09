package com.example.Inmopro.v1.Dto.User;

public class SearchRole {

    private String Rol;
    private Integer user_id;
    //private String rolName;

    public void setUserId(String user_id) {
        this.Rol = user_id;
    }
    public Integer getUserId() {
        return user_id;
    }

    public void setRol(String Rol) {
        this.Rol = Rol;
    }
    public String getRol() {
        return Rol;
    }

}

