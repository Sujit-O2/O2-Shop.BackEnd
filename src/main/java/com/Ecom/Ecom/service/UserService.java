package com.Ecom.Ecom.service;

import com.Ecom.Ecom.Entity.User;
import com.Ecom.Ecom.Repo.UserRepo;
import com.Ecom.Ecom.Roles.UserRole;
import com.Ecom.Ecom.dto.RegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepo rr;
    BCryptPasswordEncoder passwordEncoder =new BCryptPasswordEncoder(12);
    public User setUser(RegisterDto u1) {
        User u = new User();
        u.setName(u1.getName());
        u.setEmail(u1.getEmail());
        u.setRole(u1.getRole() != null ? UserRole.valueOf(u1.getRole()) : UserRole.USER);
        u.setPassword(passwordEncoder.encode(u1.getPassword()));
        u.setImg(null);
        return rr.save(u);
    }


    public ResponseEntity<List<User>> getUsers() {
        return  ResponseEntity.ok(rr.findAll()) ;
    }

    public User getUser(String gmail) {
        return rr.findByEmail(gmail).orElseThrow(()->new UsernameNotFoundException("no user")
        );
    }
}
