package com.telephone.directory.service.auth.proxy.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
