package com.knowway.auth.util;


import com.knowway.auth.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtUtil {


  private JwtUtil() {

  }

  public static String generateTokenWithClaims(String subject,String key,long duration,
      Map<String, Object> claimsList) {
    Date now = new Date();
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(now)
        .setExpiration(Date.from(Instant.now().plusSeconds(
            duration)))
        .signWith(Keys.hmacShaKeyFor(key.getBytes()), SignatureAlgorithm.HS256)
        .addClaims(claimsList)
        .compact();
  }

  public static String
  generateToken(String subject,String key,long duration) {
    Date now = new Date();
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(now)
        .setExpiration(Date.from(Instant.now().plusSeconds((duration))))
        .signWith(Keys.hmacShaKeyFor(key.getBytes()), SignatureAlgorithm.HS256)
        .compact();
  }



  public static Map<String, Object> addClaims(String id, Object value) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(id, value);
    return claims;
  }

  public static String extractTokenSubject(String token,String key) {
    return extractTokenClaims(token,key).getSubject();
  }

  public static Claims extractTokenClaims(String token, String key) {
    return Jwts.parserBuilder()
        .setSigningKey(key.getBytes()).
        build()
        .parseClaimsJws(token)
        .getBody();
  }


  public static boolean isTokenValid(String token, String key) throws AuthException {
    try {
      extractTokenClaims(token,key);
      return true;
    } catch (ExpiredJwtException e) {
      throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage()) {
      };
    } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
      throw new AuthException("유효하지 않은 토큰입니다.");
    }
  }




}

