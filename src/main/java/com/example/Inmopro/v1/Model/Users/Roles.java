package com.example.Inmopro.v1.Model.Users;

public enum Roles {
    Tenant(1),
    Owner(2),
    Monitor(3),
    Coordinator(4),
    Admin(5);

    private final int roleId;

    Roles(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }
}