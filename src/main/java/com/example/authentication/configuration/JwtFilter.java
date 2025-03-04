package com.example.authentication.configuration;

import com.example.authentication.Model.DTO.UserDetailsImpl;
import com.example.authentication.exception.ErrorCode;
import com.example.authentication.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = JwtUtils.getTokenFromRequest(request);

        // nếu trong header ko có jwt -> đi qua filter tiếp theo để xác thực login
        if(jwt.isEmpty() || jwt == null){
            doFilter(request,response, filterChain);
            return;
        }

        // extract jwt lấy username
        try{
            String username = JwtUtils.extractClaims(jwt).getSubject();

            // load userdetails
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
            userDetails.erasePassword();

            // tạo đối tượng authentication
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            // lưu vào context
            SecurityContextHolder.getContext().setAuthentication(authToken);
            doFilter(request,response, filterChain);

        }catch(ExpiredJwtException e){
            JwtUtils.handleJwtException(response, ErrorCode.TOKEN_EXPIRED.getMessage());
        }catch(UnsupportedJwtException e){
            JwtUtils.handleJwtException(response, ErrorCode.TOKEN_INVALID.getMessage());
        }catch(MalformedJwtException e){
            JwtUtils.handleJwtException(response, ErrorCode.TOKEN_INVALID.getMessage());
        }catch(SignatureException e){
            JwtUtils.handleJwtException(response, ErrorCode.SIGNATURE_INVALID.getMessage());
        }
    }
}
