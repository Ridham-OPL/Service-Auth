package com.telephone.directory.service.auth.service;

import com.telephone.directory.service.auth.proxy.request.AuthRequest;
import com.telephone.directory.service.auth.proxy.request.LoginRequest;
import com.telephone.directory.service.auth.proxy.response.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface EntryService {

    ResponseEntity<LoginResponse> login(LoginRequest login);

    ResponseEntity<Boolean> validateTokenForRequest(AuthRequest authRequest);
}
