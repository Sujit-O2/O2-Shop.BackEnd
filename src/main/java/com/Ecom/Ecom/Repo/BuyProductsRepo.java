package com.Ecom.Ecom.Repo;

import com.Ecom.Ecom.Entity.BuyProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuyProductsRepo extends JpaRepository<BuyProduct,Integer> {

    List<BuyProduct> findAllByUserEmail(String name);

    List<BuyProduct> findAllBySellerGmail(String name);
}
