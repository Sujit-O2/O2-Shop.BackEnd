package com.Ecom.Ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponce {
    private String token;
    private int id;
    private String name;
    private String email;
    private String role;
}
