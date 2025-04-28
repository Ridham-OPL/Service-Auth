package com.telephone.directory.service.auth.service.impl;

import com.telephone.directory.service.auth.proxy.request.AuthRequest;
import com.telephone.directory.service.auth.proxy.request.LoginRequest;
import com.telephone.directory.service.auth.proxy.response.LoginResponse;
import com.telephone.directory.service.auth.security.CustomUserDetailsService;
import com.telephone.directory.service.auth.service.EntryService;
import com.telephone.directory.service.auth.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class EntryServiceImpl implements EntryService {

    private final Logger logger = LoggerFactory.getLogger(EntryServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest login) {
        Authentication auth = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
        Authentication authResult = authenticationManager.authenticate(auth);
        if (authResult.isAuthenticated()) {
            String token = jwtUtils.generateToken(login.getUsername());
            logger.info("User login successfully with username : {}", login.getUsername());
            return ResponseEntity.ok().body(new LoginResponse(token, login.getUsername(),
                    authResult.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())));
        }
        return null;
    }

    @Override
    public ResponseEntity<Boolean> validateTokenForRequest(AuthRequest token) {
        String tokenValue = token.getToken().substring(7);
        String username = jwtUtils.extractUserName(tokenValue);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(jwtUtils.validateToken(tokenValue, userDetails));
    }
}
