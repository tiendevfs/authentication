package com.example.authentication.utils;

import com.example.authentication.Model.DTO.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    public static String key = "1Vnio5sQsDXo1DLdkBbbtCXjzK4eGhpeVxiOX5bBy7c=\n";
    public static long EXPIRATION = 1000 * 60 * 5; // 5 phút
    public static long REFRESHABLE = 1000 * 60 * 60 * 24 * 7; // 7 ngày

    public static String generateToken(UserDetailsImpl userDetailsImpl, long expiration){
        return Jwts.builder()
                .setSubject(userDetailsImpl.getUsername())
                .setIssuer(userDetailsImpl.getFullname())
                .claim("roles", UserDetailsImpl.convert(userDetailsImpl.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static SecretKey signKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(key));
    }

    public static Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(signKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public static void handleJwtException(HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Error", "Unauthorized");
        responseBody.put("message", message);

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
    public static String getTokenFromRequest(HttpServletRequest request){
        String jwt = "";
        String header = request.getHeader("Authorization");
        if(header != null){
            jwt = header.substring(7);
        }
        return jwt;
    }
}
