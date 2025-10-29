package com.Ecom.Ecom.Controller;

import com.Ecom.Ecom.Entity.User;
import com.Ecom.Ecom.dto.UpdateUserDto;
import com.Ecom.Ecom.service.UpdateUserService;
import com.Ecom.Ecom.service.JwtService; // Service that extracts email from JWT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/update")
public class UpdateUser {

    @Autowired
    private UpdateUserService uu;

    @Autowired
    private JwtService jwtService;

    @PutMapping
    public Optional<User> updateUser(@RequestBody UpdateUserDto userDto,
                                     Authentication authentication) {

        String email = authentication.getName();
        return uu.updateUserByEmail(email, userDto);
    }
}
