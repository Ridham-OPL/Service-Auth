package com.telephone.directory.service.auth.service;

import com.telephone.directory.service.auth.proxy.request.UserProxy;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<UserProxy> getUser(Long id);

    ResponseEntity<Void> createUser(UserProxy user);

    ResponseEntity<Void> updateUser(Long id, UserProxy user);

    ResponseEntity<Void> deleteUser(Long id);


}
