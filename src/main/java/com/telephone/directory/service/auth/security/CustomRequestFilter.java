package com.telephone.directory.service.auth.security;

import com.telephone.directory.service.auth.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Component
public class CustomRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            if (request.getHeader("Authorization") != null) {
                String bearer = request.getHeader("Authorization");
                String token = bearer.substring(7);
                String username = jwtUtils.extractUserName(token);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtils.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String jsonResponse = String.format(
                    "{\"timestamp\": \"%s\", \"class\": \"%s\",\"message\": \"%s\",\"path\": \"%s\"}",
                    Instant.now().toString(),
                    e.getClass(), getExceptionMessage(e), request.getServletPath());
            response.getWriter().write(jsonResponse);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getExceptionMessage(JwtException e) {
        return switch (e) {
            case SignatureException signatureException -> "Something went wrong in signature";
            case MalformedJwtException malformedJwtException -> "JWT token is not correctly constructed";
            case ExpiredJwtException expiredJwtException -> "JWT token is expired";
            case null, default -> "Something went wrong in Token";
        };
    }
}
