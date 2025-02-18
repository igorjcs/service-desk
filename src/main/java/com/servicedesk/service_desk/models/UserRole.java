package com.servicedesk.service_desk.models;

public enum UserRole {
    ADMIN("admin"),
    USER("admin");

    private String role;

    UserRole (String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }

}
