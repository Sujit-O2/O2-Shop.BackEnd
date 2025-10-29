package com.Ecom.Ecom.dto;

import com.Ecom.Ecom.Roles.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class userProfileDto {
    private int id;
    private String name;
    private String email;
    private UserRole role;
    private String address;
    private String profileImageBase64;
}
