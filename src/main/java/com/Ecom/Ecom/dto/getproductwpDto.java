package com.Ecom.Ecom.dto;

import com.Ecom.Ecom.Entity.productPhoto;
import lombok.Data;

import java.util.List;
@Data
public class getproductwpDto {
    private int pid;
    private String sellername;
    private String pname;
    private String description;
    private int price;
    private String stock;
    private int status;
    private List<byte[]> img;
}
