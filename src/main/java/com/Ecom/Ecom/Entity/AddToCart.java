// src/main/java/com/Ecom/Ecom/Entity/AddToCart.java
package com.Ecom.Ecom.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AddToCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_mail", nullable=false)
    private String userMail;
    private int pid;

    @Column(nullable=false)
    private Integer quantity = 1;
}
