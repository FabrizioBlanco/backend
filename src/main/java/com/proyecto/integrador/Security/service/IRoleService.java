package com.proyecto.integrador.Security.service;

import com.proyecto.integrador.Security.model.Role;
import com.proyecto.integrador.Security.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name);
}
