package com.emintufan.labreportingapp.security.service;

import com.emintufan.labreportingapp.security.constants.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claim = extractAllClaims(token);
        return claimResolver.apply(claim);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] key = Decoders.BASE64.decode(SecurityConstant.SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    public String generateToken(UserDetailsImpl userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }

    public String generateToken(UserDetailsImpl userDetails, HashMap<String, Object> extraClaims) {
        List<String> authorities = userDetails.getAuthorities().
                stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .claims(extraClaims)
                .claim("authorities", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .subject(userDetails.getUsername())
                .signWith(getSignKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetailsImpl userDetails) {
        String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }

}
