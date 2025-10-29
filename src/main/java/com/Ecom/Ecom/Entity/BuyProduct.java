package com.Ecom.Ecom.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class BuyProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userEmail;
    private String sellerGmail;
    private int quantity;

    private String address;
    private int price;
    private int pid;

    private String mode;
    private String status;
    private LocalDateTime purchaseDate;
    private LocalDate dateApply;
    private LocalDate deDate;


}
