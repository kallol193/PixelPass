package com.BekaarCompany.PixelPass.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {

    private String secretkey ="943bd9f80dd4d9f6ed8ec368403f3b7eb7409bf61c8c48295fb5f13d2ee975c4ln";

    //token generator
    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,secretkey)
                .compact();
    }

    //token validator
    public boolean validateToken(String token,String username){
        String tokenUsername = extractUsername(username);
        return (tokenUsername.equals(username) && !isTokenExpired(token));


    }
    private Claims extractClaims(String token){
        return Jwts.parser()
                .setSigningKey(secretkey)
                .parseClaimsJwt(token)
                .getBody();
    }

    //extract username
    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    private Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
}
