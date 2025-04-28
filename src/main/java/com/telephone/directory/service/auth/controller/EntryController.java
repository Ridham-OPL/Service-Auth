package com.telephone.directory.service.auth.controller;

import com.telephone.directory.service.auth.proxy.request.AuthRequest;
import com.telephone.directory.service.auth.proxy.request.LoginRequest;
import com.telephone.directory.service.auth.proxy.response.LoginResponse;
import com.telephone.directory.service.auth.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntryController {

    @Autowired
    private EntryService entryService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return entryService.login(loginRequest);
    }

    @PostMapping("/validate-request")
    public ResponseEntity<Boolean> validateRequest(@RequestBody AuthRequest authRequest) {
        return entryService.validateTokenForRequest(authRequest);
    }
}
