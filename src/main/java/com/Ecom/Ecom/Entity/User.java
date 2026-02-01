package com.Ecom.Ecom.Entity;

import com.Ecom.Ecom.Roles.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
//@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Lob
//    @Column(name = "img", columnDefinition = "BYTEA")
    private byte[] img;
    private String address;

    @Column(nullable = false)
    private String password;
}
