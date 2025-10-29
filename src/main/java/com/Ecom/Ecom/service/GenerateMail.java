package com.Ecom.Ecom.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateMail {
    @Autowired
    private JwtService service;


    public String getMale(HttpServletRequest httpServletRequest) {
        if(httpServletRequest.getCookies()!=null){
            for(Cookie cc: httpServletRequest.getCookies()){
                if(cc.getName().equals("token")){
                    return service.getUser(cc.getValue());
                }
            }
        }
        return  null;
    }
}
