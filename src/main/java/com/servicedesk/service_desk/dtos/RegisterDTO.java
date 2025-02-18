package com.servicedesk.service_desk.dtos;

import com.servicedesk.service_desk.models.UserRole;

public record RegisterDTO(String username, String password, UserRole role) {

}
