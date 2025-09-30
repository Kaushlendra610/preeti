package com.preetinest.service;

import com.preetinest.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUuid(String uuid);
    List<User> getAllActiveUsers();
    User updateUser(Long id, User user);
    void softDeleteUser(Long id);
}