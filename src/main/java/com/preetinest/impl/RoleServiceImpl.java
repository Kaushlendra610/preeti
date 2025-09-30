 package com.preetinest.impl;

import com.preetinest.entity.Role;
import com.preetinest.repository.RoleRepository;
import com.preetinest.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll().stream()
                .filter(role -> role.getDeleteStatus() == 2)
                .toList();
    }

    @Override
    public Optional<Role> getRoleByUuid(String uuid) {
        return roleRepository.findByUuid(uuid)
                .filter(role -> role.getDeleteStatus() == 2);
    }

    @Override
    public Role createRole(Role role) {
        role.setUuid(UUID.randomUUID().toString());
        role.setDeleteStatus(2);
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(String uuid, Role roleDetails) {
        Optional<Role> roleOptional = roleRepository.findByUuid(uuid);
        if (roleOptional.isEmpty() || roleOptional.get().getDeleteStatus() == 1) {
            throw new RuntimeException("Role not found with UUID: " + uuid);
        }
        Role role = roleOptional.get();
        role.setName(roleDetails.getName());
        role.setDescription(roleDetails.getDescription());
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(String uuid) {
        Optional<Role> roleOptional = roleRepository.findByUuid(uuid);
        if (roleOptional.isEmpty() || roleOptional.get().getDeleteStatus() == 1) {
            throw new RuntimeException("Role not found with UUID: " + uuid);
        }
        Role role = roleOptional.get();
        role.setDeleteStatus(1);
        roleRepository.save(role);
    }
}
