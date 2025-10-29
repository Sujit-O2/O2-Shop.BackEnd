package com.Ecom.Ecom.Controller;

import com.Ecom.Ecom.Entity.BuyProduct;
import com.Ecom.Ecom.Repo.BuyProductsRepo;
import com.Ecom.Ecom.dto.ProductPhotoDto;
import com.Ecom.Ecom.dto.ShowOrderDto;
import com.Ecom.Ecom.dto.UpdateProductDto;
import com.Ecom.Ecom.dto.productDto;
import com.Ecom.Ecom.service.OrderService;
import com.Ecom.Ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BuyProductsRepo buyProductsRepository;
    @Autowired
    private OrderService service;
//    @GetMapping("/dashboard")
    public String sellerDashboard() {
        return "Welcome Seller!";
    }

    @GetMapping("/myOrders")
    @PreAuthorize("hasRole('SELLER')")
    public List<ShowOrderDto> getSellerOrders(Authentication authentication) {
        return service.ManageOrdersofUser(authentication.getName());
    }

    @PutMapping("/orders/{id}/status")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> updateOrderStatus(@PathVariable int id, @RequestBody Map<String, String> body,Authentication authentication) {
        String status = body.get("status");
        System.out.println("bye");
        BuyProduct order = buyProductsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        buyProductsRepository.save(order);
        return ResponseEntity.ok("Status updated");
    }

    @DeleteMapping("/orders/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> deleteOrder(@PathVariable int id,Authentication authentication) {
        System.out.println("hii");
        buyProductsRepository.deleteById(id);
        return ResponseEntity.ok("Order deleted");
    }

    @PostMapping("/addProducts")
    public ResponseEntity<String> addProduct(@RequestBody productDto pd, Authentication authentication) {
        try {
            String sellerEmail = authentication.getName();
            productDto savedProduct = productService.addProduct(pd, sellerEmail);
            return ResponseEntity.ok("cool");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add product: " + e.getMessage());
        }
    }
    @GetMapping("/products")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<productDto>> getProductsForSeller(Authentication authentication) {
        String sellerEmail = authentication.getName();
        return ResponseEntity.ok(productService.getProductsBySellername(sellerEmail));
    }
    @PutMapping("/updateProduct/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @RequestBody UpdateProductDto dto,
            Authentication authentication) {
        try {
            String sellerEmail = authentication.getName();
            productDto updatedProduct = productService.updateProduct(id, dto, sellerEmail);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update product: " + e.getMessage());
        }
    }
    @PostMapping("/products/{productId}/addPhoto")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> addProductPhoto(
            @PathVariable int productId,
            @RequestBody ProductPhotoDto photoDto,
            Authentication authentication) {
        try {
            String sellerEmail = authentication.getName();
            productService.addPhotoToProduct(productId, photoDto.getPhoto(), sellerEmail);
            return ResponseEntity.ok("Photo added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add photo: " + e.getMessage());
        }
    }
    @GetMapping("/products/search")
    public List<productDto> SellerProduct(@RequestParam String keyword,Authentication authentication){
        return productService.SearchSellerProducts(keyword,authentication.getName());
    }


}
