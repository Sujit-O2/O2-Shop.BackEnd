package com.Ecom.Ecom.service;

import com.Ecom.Ecom.Entity.User;
import com.Ecom.Ecom.Repo.UserRepo;
import com.Ecom.Ecom.dto.UpdateUserDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.Optional;

@Service
@Transactional
public class UpdateUserService {
    @Autowired
    private UserRepo rr;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public Optional<User> updateUserByEmail(String email, UpdateUserDto dto) {
        Optional<User> userOpt = rr.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
                if (dto.getName() != null) user.setName(dto.getName());
                if (dto.getPhoto() != null) user.setImg(Base64.getDecoder().decode(dto.getPhoto().split(",")[1]));
            return Optional.of(rr.save(user));
        }
        return Optional.empty();
    }

    public Optional<User> updateUser(int id, UpdateUserDto userDto){


        User user=rr.findById(id).orElseThrow(()->new UsernameNotFoundException("no user"));

    if(userDto.getName()!=null&&!userDto.getName().trim().isEmpty()){
        user.setName(userDto.getName().trim());

    }
    if(userDto.getPhoto()!=null&&!userDto.getPhoto().isEmpty()){
        if (userDto.getPhoto().contains(",")) {
            userDto.setPhoto(userDto.getPhoto().split(",")[1]);
        }
        byte []bb= Base64.getDecoder().decode(userDto.getPhoto());
        user.setImg(bb);
    }

        rr.save(user);
        return Optional.of(user);



    }
}
