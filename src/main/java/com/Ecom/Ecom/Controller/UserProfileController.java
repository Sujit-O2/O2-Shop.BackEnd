package com.Ecom.Ecom.Controller;

import com.Ecom.Ecom.Entity.User;
import com.Ecom.Ecom.Repo.UserRepo;
import com.Ecom.Ecom.dto.userProfileDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@CrossOrigin
@RestController
@RequestMapping("/profile")
public class UserProfileController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public ResponseEntity<userProfileDto> getUserProfile(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        userProfileDto profile = new userProfileDto();
        profile.setId(user.getId());
        profile.setName(user.getName());
        profile.setEmail(user.getEmail());
        profile.setAddress(user.getAddress());
        profile.setRole(user.getRole());

        if (user.getImg() != null) {
            profile.setProfileImageBase64(Base64.getEncoder().encodeToString(user.getImg()));
        }

        return ResponseEntity.ok(profile);
    }
}
