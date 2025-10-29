package com.Ecom.Ecom.service;

import com.Ecom.Ecom.Entity.User;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
@Service
public class JwtService {
    private  SecretKey key=Jwts.SIG.HS256.key().build();


    public String generatetoken(User a){

        return Jwts.builder()
                .claim("role",a.getRole())
                .subject(a.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(key)
                .compact();

    }


    public String getUser(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean ValidateToken(String Token,UserDetails userDetails){
        String username=getUser(Token);
        return username.equals(userDetails.getUsername())&&!isTokenExpaired(Token);
    }

    private boolean isTokenExpaired(String token) {
        Date exp=Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload().getExpiration();

        return exp.before(new Date());
    }
}
