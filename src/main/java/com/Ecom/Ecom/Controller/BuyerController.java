// src/main/java/com/Ecom/Ecom/Controller/BuyerController.java
package com.Ecom.Ecom.Controller;

import com.Ecom.Ecom.Entity.AddToCart;
import com.Ecom.Ecom.Entity.BuyProduct;
import com.Ecom.Ecom.Entity.Products;
import com.Ecom.Ecom.Entity.productPhoto;
import com.Ecom.Ecom.dto.*;
import com.Ecom.Ecom.service.OrderService;
import com.Ecom.Ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class BuyerController {
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService serviceOrder;

    @PostMapping("/Addtocart/{id}")
    public String Addtocart(@PathVariable int id, Authentication authentication) {
        return productService.Addtocart(id, authentication.getName());
    }

    @PostMapping("/BuyNow")
    public String BuyNow(@RequestBody BuyProduct product, Authentication authentication) {
        return productService.BuyNow(product, authentication.getName());
    }

    // /user/cart
    @GetMapping("/cart")
    public ResponseEntity<List<CartItemDto>> Cart(Authentication authentication){
        return productService.getCartProducts(authentication.getName());
    }


    @DeleteMapping("/cart/remove/{productid}")
    public ResponseEntity<Void> remove(@PathVariable int productid, Authentication authentication) {
        productService.removeFromCartByProductId(productid, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cart/update/{productId}")
    public ResponseEntity<Void> updateQty(
            @PathVariable int productId,
            @RequestBody QuantityDto body,
            Authentication authentication
    ) {
        productService.updateQuantityByProductId(productId, body.getQuantity(), authentication.getName());
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/orders")
    public List<ShowOrderDto> orders(Authentication authentication){

        return serviceOrder.getAllorderofUser(authentication.getName());

    }
    @GetMapping("/products/{id}")
    public ResponseEntity<getproductwpDto> getProductInfo(@PathVariable int id){
        Products pp = productService.getProductByid(id);
        getproductwpDto dp=new getproductwpDto();
        dp.setPid(pp.getId());
        dp.setPrice(pp.getPrice());
        dp.setPname(pp.getName());
        dp.setSellername(pp.getSellername());
        dp.setStatus(pp.getStatus());
        dp.setStock(String.valueOf(pp.getStock()));
        dp.setDescription(pp.getDescription());
        List<byte[]> photos = pp.getPhotos().stream()
                .map(productPhoto::getPhoto)
                .collect(Collectors.toList());
        dp.setImg(photos);
        return ResponseEntity.ok(dp);
    }

}
