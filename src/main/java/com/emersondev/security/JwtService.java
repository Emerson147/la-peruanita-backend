package com.emersondev.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private long expiration;

  // Generar token con rol incluido
  public String generateToken(UserDetails userDetails, String rol) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("rol", rol);

    return Jwts.builder()
            .claims(claims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(
                    System.currentTimeMillis() + expiration))
            .signWith(getSigningKey())
            .compact();
  }

  // Extraer email del token
  public String extractEmail(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // Extraer rol del token
  public String extractRol(String token) {
    return extractClaim(token,
            claims -> claims.get("rol", String.class));
  }

  // Validar token
  public boolean isTokenValid(String token,
                              UserDetails userDetails) {
    final String email = extractEmail(token);
    return email.equals(userDetails.getUsername())
            && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private <T> T extractClaim(String token,
                             Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(
            secret.getBytes(StandardCharsets.UTF_8));
  }

}


