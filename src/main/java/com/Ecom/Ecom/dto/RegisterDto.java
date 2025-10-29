package com.Ecom.Ecom.dto;

import com.Ecom.Ecom.Roles.UserRole;
import lombok.Data;

@Data
public class RegisterDto {
    private String name;
    private String email;
    private String password;
    private String role;
}
