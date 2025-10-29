package com.Ecom.Ecom.dto;

import com.Ecom.Ecom.Entity.Products;

import java.util.ArrayList;

public class ProductMapper {

    // Entity to DTO
    public productDto toDto(Products product) {
        productDto dto = new productDto();
        dto.setPid(product.getId());
        dto.setPname(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(String.valueOf(product.getStock())); // Convert int to String
        dto.setSellername(product.getSellername());
        dto.setStatus(product.getStatus());
        dto.setCategory(product.getCategory());
        if(product.getPhotos() != null && !product.getPhotos().isEmpty())
        dto.setImg(product.getPhotos().getFirst().getPhoto());
        else
            dto.setImg(null);

        return dto;
    }

    // DTO to Entity
    public Products toEntity(productDto dto) {
        Products product = new Products();
        product.setId(dto.getPid());
        product.setName(dto.getPname());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(Integer.parseInt(dto.getStock()));
        product.setSellername(dto.getSellername());
        product.setStatus(dto.getStatus());
        product.setCategory(dto.getCategory());
        product.setPhotos(new ArrayList<>());
        return product;
    }
}