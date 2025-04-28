package com.telephone.directory.service.auth.service.impl;

import com.telephone.directory.service.auth.domain.User;
import com.telephone.directory.service.auth.proxy.request.UserProxy;
import com.telephone.directory.service.auth.reposotory.UserRepository;
import com.telephone.directory.service.auth.service.UserService;
import com.telephone.directory.service.auth.utils.MapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<UserProxy> getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return ResponseEntity.ok().body(MapperHelper.convertor(user, UserProxy.class));
    }

    @Override
    public ResponseEntity<Void> createUser(UserProxy user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(MapperHelper.convertor(user, User.class));
        logger.info("User successfully created.");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateUser(Long id, UserProxy userProxy) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(userProxy.getUsername());
        user.setFirstName(userProxy.getFirstName());
        user.setEmail(userProxy.getEmail());
        user.setRoles(userProxy.getRoles());
        user.setPassword(userProxy.getPassword());
        user.setName(userProxy.getName());
        userRepository.save(user);
        logger.info("User successfully updated with id: {}", id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
