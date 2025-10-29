package com.Ecom.Ecom.Repo;

import com.Ecom.Ecom.Entity.AddToCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AddtoCartRepo extends JpaRepository<AddToCart,Integer> {

    List<AddToCart> findByUserMail(String userMail);

    Optional<AddToCart> findByUserMailAndPid(String userMail, int productid);

//    @Modifying
//    @Transactional
    void deleteByUserMailAndPid(String userMail, int productId);


//    Optional<AddToCart> findByIdAndUserMail(Integer id, String userMail);

}
