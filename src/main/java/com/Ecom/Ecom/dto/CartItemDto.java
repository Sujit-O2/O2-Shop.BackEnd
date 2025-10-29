package com.Ecom.Ecom.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private int id;
    private int quantity;
    private productDto product;

}
