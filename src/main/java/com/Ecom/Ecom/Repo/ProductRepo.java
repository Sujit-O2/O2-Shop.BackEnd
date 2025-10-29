package com.Ecom.Ecom.Repo;

import com.Ecom.Ecom.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepo extends JpaRepository<Products,Integer> {
    List<Products> findBySellername(String SellerGmail);
    @Query("SELECT p FROM Products p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :key, '%')) " +
            "OR LOWER(p.category) LIKE LOWER(CONCAT('%', :key, '%'))")
    public List<Products> findByKey(String key);

    @Query("SELECT p FROM Products p WHERE (LOWER(p.name) like lower(CONCAT('%',:key,'%')) "
    +"OR LOWER(p.category) LIKE LOWER(CONCAT('%', :key, '%'))) "+"AND LOWER(p.sellername) like lower( :name)")
    List<Products> findBySellerKey(String key, String name);
}
