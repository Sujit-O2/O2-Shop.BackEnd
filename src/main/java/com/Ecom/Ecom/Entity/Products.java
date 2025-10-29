package com.Ecom.Ecom.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;
    private String description;
    private int price;
    private int stock;
    private String sellername;
    private int status;
    private String category;

    // One Product → Many Photos
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<productPhoto> photos;
}

