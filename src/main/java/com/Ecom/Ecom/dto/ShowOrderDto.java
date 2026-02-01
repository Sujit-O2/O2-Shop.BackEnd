package com.Ecom.Ecom.dto;
import com.Ecom.Ecom.Entity.BuyProduct;
import com.Ecom.Ecom.Entity.Products;
import com.Ecom.Ecom.service.ProductService;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ShowOrderDto {
    private int id;
    private int quantity;
    private String address;
    private int price;
    private int pid;

    private String mode;
    private String status;
    private LocalDateTime purchaseDate;
    private LocalDate deDate;

    private String pname;
    private String img;


    public List<ShowOrderDto> GetDtos(List<BuyProduct> bb,ProductService productService) {

        List<ShowOrderDto> dto=new ArrayList<>();
        for(BuyProduct b1: bb) {
            ShowOrderDto d1 = new ShowOrderDto();
            d1.setId(b1.getId());
            d1.setPid(b1.getPid());
            d1.setMode(b1.getMode());
            d1.setAddress(b1.getAddress());
            d1.setPrice(b1.getPrice());
            d1.setQuantity(b1.getQuantity());
            d1.setStatus(b1.getStatus());
            d1.setPurchaseDate(b1.getPurchaseDate());
            d1.setDeDate(b1.getDeDate());
            System.out.println(d1.getPid());

            Products pp = productService.getProductByid(d1.getPid());
            System.out.println(d1.getPid());
            ProductMapper mm=new ProductMapper();

            productDto od=mm.toDto(pp);;



            d1.setPname(pp.getName());
            d1.setImg(od.getImg());

//



            dto.add(d1);
        }

        return dto;
    }
}
