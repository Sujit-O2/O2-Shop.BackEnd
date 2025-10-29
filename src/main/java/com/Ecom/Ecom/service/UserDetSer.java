package com.Ecom.Ecom.service;

import com.Ecom.Ecom.Entity.User;
import com.Ecom.Ecom.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetSer implements UserDetailsService {
    @Autowired
    UserRepo ur;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User uu=ur.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("user not found"));
        return new myUser(uu);
    }
}
