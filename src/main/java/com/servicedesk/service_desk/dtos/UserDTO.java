package com.servicedesk.service_desk.dtos;

import com.servicedesk.service_desk.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String username;
    private String password;
    private UserRole role;
}
