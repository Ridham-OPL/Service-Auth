package com.telephone.directory.service.auth.proxy.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProxy {
    private Long id;

    private String username;

    private String password;

    private String name;

    private String firstName;

    private String roles;

    private String email;
}
