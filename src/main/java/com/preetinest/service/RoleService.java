        package com.preetinest.service;

import com.preetinest.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRoles();
    Optional<Role> getRoleByUuid(String uuid);
    Role createRole(Role role);
    Role updateRole(String uuid, Role roleDetails);
    void deleteRole(String uuid);
}
