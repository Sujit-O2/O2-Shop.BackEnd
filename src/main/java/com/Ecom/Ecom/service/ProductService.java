// src/main/java/com/Ecom/Ecom/service/ProductService.java
package com.Ecom.Ecom.service;

import com.Ecom.Ecom.Entity.*;
import com.Ecom.Ecom.Repo.AddtoCartRepo;
import com.Ecom.Ecom.Repo.ProductRepo;
import com.Ecom.Ecom.Repo.UserRepo;
import com.Ecom.Ecom.dto.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepository;
    @Autowired
    private AddtoCartRepo addtoCartRepo;
    @Autowired
    private UserRepo userRepo;

    public List<productDto> getProductsBySellername(String sellerEmail) {
        List<Products> products = productRepository.findBySellername(sellerEmail);
        List<productDto> dto = new ArrayList<>();
        ProductMapper pm = new ProductMapper();
        for (Products p : products) {
            dto.add(pm.toDto(p));
        }
        return dto;
    }

    public productDto addProduct(productDto pd, String sellerEmail) {
        User seller = userRepo.findByEmail(sellerEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        if (!seller.getRole().name().equals("SELLER")) {
            throw new RuntimeException("Only sellers can add products");
        }
        pd.setSellername(sellerEmail);
        Products product = new ProductMapper().toEntity(pd);
        product = productRepository.save(product);
        return new ProductMapper().toDto(product);
    }

    public List<productDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> new ProductMapper().toDto(product))
                .collect(Collectors.toList());
    }


    public productDto updateProduct(int productId, UpdateProductDto dto, String sellerEmail) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (!product.getSellername().equals(sellerEmail)) {
            throw new RuntimeException("You are not the owner of this product");
        }
        product.setName(dto.getPname());
        product.setDescription(dto.getDescription());
        product.setPrice((int) dto.getPrice());
        product.setStock(dto.getStock());
        product.setStatus(Integer.parseInt(dto.getStatus()));
        productRepository.save(product);
        return new ProductMapper().toDto(product);
    }

    public void addPhotoToProduct(int productId, byte[] photo, String sellerEmail) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (!product.getSellername().equals(sellerEmail)) {
            throw new RuntimeException("You are not the owner of this product");
        }
        productPhoto pp = new productPhoto();
        pp.setPhoto(photo);
        pp.setProduct(product);
        product.getPhotos().add(pp);
        productRepository.save(product);
    }

    public Products getProductByid(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("product Not available"));
    }

    // Merge-on-add to keep one row per product per user

    public String Addtocart(int productId, String userMail) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        AddToCart row = (AddToCart) addtoCartRepo.findByUserMailAndPid(userMail, productId)
                .orElse(null);
        if (row == null) {
            row = new AddToCart();
            row.setUserMail(userMail);
            row.setPid(productId);
            row.setQuantity(1);
        } else {
            row.setQuantity(row.getQuantity() + 1);
        }
        addtoCartRepo.save(row);
        return "success";
    }

    public String BuyNow(BuyProduct product, String name) {
        return "commig Soon";
    }

    public ResponseEntity<List<CartItemDto>> getCartProducts(String userMail) {
        List<AddToCart> list = addtoCartRepo.findByUserMail(userMail);
        List<CartItemDto> cc=new ArrayList<>();
        for(AddToCart aa: list){
            CartItemDto ad=new CartItemDto();
            ad.setId(aa.getId());
            ad.setQuantity(aa.getQuantity());
            Products product=productRepository.findById(aa.getPid()).orElseThrow(RuntimeException::new);
            ProductMapper pp=new ProductMapper();
            ad.setProduct(pp.toDto(product));
            cc.add(ad);
        }
        return ResponseEntity.ok(cc);
    }

@Transactional
    public void removeFromCartByProductId(int productId, String userMail) {
        addtoCartRepo.deleteByUserMailAndPid(userMail, productId);
    }
@Transactional
    public void updateQuantityByProductId(int productId, int quantity, String userMail) {
        if (quantity < 1) addtoCartRepo.deleteById(productId);
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        AddToCart row = (AddToCart) addtoCartRepo.findByUserMailAndPid(userMail, productId)
                .orElseThrow(() -> new RuntimeException("Cart row not found"));
        row.setQuantity(quantity);
        addtoCartRepo.save(row);
    }

    public String GetSellerGmail(int pid) {
       Products pp= productRepository.findById(pid).orElseThrow(()->new RuntimeException("Product user name not found"));
        return pp.getSellername();
    }

    @Transactional
    public void decreaseStock(int pid, int quantity) {
        Products product = productRepository.findById(pid)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be >= 1");
        }
        if (product.getStock() < quantity) {
            throw new IllegalStateException("Insufficient stock");
        }

        product.setStock(product.getStock() - quantity);
        // save implicit on transaction flush; @Version ensures optimistic lock [web:21]
    }
    public List<productDto> SearchProducts(String keyword) {
        List<productDto>pp=new ArrayList<>();
        List<Products> l1=productRepository.findByKey(keyword);
        ProductMapper mm=new ProductMapper();

        for(Products p: l1){
            pp.add(mm.toDto(p));


        }
        return  pp;
    }

    public List<productDto> SearchSellerProducts(String keyword, String name) {
        List<productDto>pp=new ArrayList<>();
        List<Products> l1=productRepository.findBySellerKey(keyword,name);
        ProductMapper mm=new ProductMapper();

        for(Products p: l1){
            pp.add(mm.toDto(p));


        }
        return  pp;

    }
}
