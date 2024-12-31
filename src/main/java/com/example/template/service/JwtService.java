package com.example.template.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {



   @Value("${spring.jwtSecret}")
    private String secretKey;



    public String extractUsername(String token){
        return extractClaims(token,Claims::getSubject);
    }


    public boolean isTokenValid(String token, String email){
        String username = extractUsername(token);
        return email.equals(username);
    }


    public String generateToken(String userEmail){
        return generateToken(new HashMap<>(),userEmail);
    }


    private String generateToken(Map<String,Object> claims, String userEmail){

        Date issueDate = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);

        return Jwts.builder().issuedAt(issueDate).expiration(expiryDate)
                .subject(userEmail).signWith(getSigningKey())
                .compact();
    }



    private boolean isTokenExpired(String token){
            return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }


    private Date extractExpiration(String token){
        return extractClaims(token,Claims::getExpiration);
    }


    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }



    private SecretKey getSigningKey(){
     byte[]keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
