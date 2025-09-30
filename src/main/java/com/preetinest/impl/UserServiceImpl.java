package com.preetinest.impl;

import com.preetinest.dto.request.UserRequestDTO;
import com.preetinest.entity.Role;
import com.preetinest.entity.User;
import com.preetinest.repository.RoleRepository;
import com.preetinest.repository.UserRepository;
import com.preetinest.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Map<String, Object> createUser(UserRequestDTO requestDTO) {
        Role role = roleRepository.findById(requestDTO.getRoleId())
                .filter(r -> r.getDeleteStatus() == 2)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + requestDTO.getRoleId()));

        User user = new User();
        user.setUuid(UUID.randomUUID().toString());
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setMobile(requestDTO.getMobile());
        user.setFacebook(requestDTO.getFacebook());
        user.setLinkedin(requestDTO.getLinkedin());
        user.setTwitter(requestDTO.getTwitter());
        user.setMetaTitle(requestDTO.getMetaTitle());
        user.setMetaKeyword(requestDTO.getMetaKeyword());
        user.setMetaDescription(requestDTO.getMetaDescription());
        user.setRole(role);
        user.setDeleteStatus(2);

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public Optional<Map<String, Object>> getUserById(Long id) {
        return userRepository.findById(id)
                .filter(u -> u.getDeleteStatus() == 2)
                .map(this::mapToResponse);
    }

    @Override
    public Optional<Map<String, Object>> getUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid)
                .filter(u -> u.getDeleteStatus() == 2)
                .map(this::mapToResponse);
    }

    @Override
    public List<Map<String, Object>> getAllActiveUsers() {
        return userRepository.findByDeleteStatus(2)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> updateUser(Long id, UserRequestDTO requestDTO) {
        User existingUser = userRepository.findById(id)
                .filter(u -> u.getDeleteStatus() == 2)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        Role role = roleRepository.findById(requestDTO.getRoleId())
                .filter(r -> r.getDeleteStatus() == 2)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + requestDTO.getRoleId()));

        existingUser.setName(requestDTO.getName());
        existingUser.setEmail(requestDTO.getEmail());
        existingUser.setMobile(requestDTO.getMobile());
        existingUser.setFacebook(requestDTO.getFacebook());
        existingUser.setLinkedin(requestDTO.getLinkedin());
        existingUser.setTwitter(requestDTO.getTwitter());
        existingUser.setMetaTitle(requestDTO.getMetaTitle());
        existingUser.setMetaKeyword(requestDTO.getMetaKeyword());
        existingUser.setMetaDescription(requestDTO.getMetaDescription());
        existingUser.setRole(role);

        User updatedUser = userRepository.save(existingUser);
        return mapToResponse(updatedUser);
    }

    @Override
    public void softDeleteUser(Long id, Long userId) {
        User user = userRepository.findById(id)
                .filter(u -> u.getDeleteStatus() == 2)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setDeleteStatus(1);
        userRepository.save(user);
        // Note: userId can be used for logging/auditing purposes
    }

    private Map<String, Object> mapToResponse(User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("uuid", user.getUuid());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("mobile", user.getMobile());
        response.put("facebook", user.getFacebook());
        response.put("linkedin", user.getLinkedin());
        response.put("twitter", user.getTwitter());
        response.put("metaTitle", user.getMetaTitle());
        response.put("metaKeyword", user.getMetaKeyword());
        response.put("metaDescription", user.getMetaDescription());
        response.put("roleId", user.getRole() != null ? user.getRole().getId() : null);
        return response;
    }
}
