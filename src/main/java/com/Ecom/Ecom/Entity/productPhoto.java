package com.Ecom.Ecom.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class productPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "photo", columnDefinition = "BYTEA")
    private byte[] photo;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;
}
