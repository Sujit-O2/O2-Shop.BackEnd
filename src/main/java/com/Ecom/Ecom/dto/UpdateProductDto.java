package com.Ecom.Ecom.dto;

import lombok.Data;

@Data
public class UpdateProductDto {
    private String pname;
    private String description;
    private double price;
    private int stock;
    private String status; // keep as String for flexibility

}
