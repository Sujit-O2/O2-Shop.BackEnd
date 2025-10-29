package com.Ecom.Ecom.service;

import com.Ecom.Ecom.Entity.BuyProduct;
import com.Ecom.Ecom.Repo.BuyProductsRepo;
import com.Ecom.Ecom.dto.ShowOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class OrderService {
    @Autowired
    private BuyProductsRepo os;
    @Autowired
    private ProductService productService;
    public List<ShowOrderDto> getAllorderofUser(String name) {
        List<BuyProduct> bb=os.findAllByUserEmail(name);
        ShowOrderDto dto=new ShowOrderDto();
        List<ShowOrderDto> orders=dto.GetDtos(bb,productService);
        return orders;



    }

    public List<ShowOrderDto> ManageOrdersofUser(String name) {

        List<BuyProduct> bb=os.findAllBySellerGmail(name);
        ShowOrderDto dto=new ShowOrderDto();
        List<ShowOrderDto> orders=dto.GetDtos(bb,productService);

        return orders;
    }
}
