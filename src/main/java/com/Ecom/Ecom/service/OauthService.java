package com.Ecom.Ecom.service;

import com.Ecom.Ecom.Entity.User;
import com.Ecom.Ecom.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OauthService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        User user = userRepo.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(oAuth2User.getAttribute("name"));
            user.setPassword(UUID.randomUUID().toString());

            userRepo.save(user);
        }

        return oAuth2User;
    }
}
