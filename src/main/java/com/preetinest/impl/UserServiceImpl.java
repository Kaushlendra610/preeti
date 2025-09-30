package com.preetinest.impl;

import com.preetinest.entity.User;
import com.preetinest.repository.UserRepository;
import com.preetinest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if (user.getUuid() == null) {
            user.setUuid(UUID.randomUUID().toString());
        }
        user.setDeleteStatus(2); // Ensure new user is not deleted
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id)
                .filter(u -> u.getDeleteStatus() == 2);
    }

    @Override
    public Optional<User> getUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid)
                .filter(u -> u.getDeleteStatus() == 2);
    }

    @Override
    public List<User> getAllActiveUsers() {
        return userRepository.findByDeleteStatus(2);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .filter(u -> u.getDeleteStatus() == 2)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setMobile(updatedUser.getMobile());
        existingUser.setFacebook(updatedUser.getFacebook());
        existingUser.setLinkedin(updatedUser.getLinkedin());
        existingUser.setTwitter(updatedUser.getTwitter());
        existingUser.setMetaTitle(updatedUser.getMetaTitle());
        existingUser.setMetaKeyword(updatedUser.getMetaKeyword());
        existingUser.setMetaDescription(updatedUser.getMetaDescription());
        existingUser.setRole(updatedUser.getRole());

        return userRepository.save(existingUser);
    }

    @Override
    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> u.getDeleteStatus() == 2)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setDeleteStatus(1);
        userRepository.save(user);
    }
}
