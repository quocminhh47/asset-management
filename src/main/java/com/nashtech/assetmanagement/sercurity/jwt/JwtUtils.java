package com.nashtech.assetmanagement.sercurity.jwt;

import com.nashtech.assetmanagement.sercurity.userdetail.UserPrinciple;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    final private String jwtSecret;

    final private int jwtExpirationMs;


    public JwtUtils(@Value("${jwt.secret-key}") final String jwtSecret,
                    @Value("${jwt.expirationMs}") final int jwtExpirationMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateJwtToken(Authentication authentication) {
        //get user information
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    //get username from Jwt token
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret) //secrect key
                .parseClaimsJws(token) //Jwt token which need to be parse
                .getBody() //take body content - this include username
                .getSubject(); //this is username
    }

    //validate token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT : {}", e.getMessage());
        }
        return false;
    }

}
