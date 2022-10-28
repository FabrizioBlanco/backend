package com.proyecto.integrador.Security.service.impl;

import com.proyecto.integrador.Security.model.Role;
import com.proyecto.integrador.Security.model.RoleName;
import com.proyecto.integrador.Security.repository.IRoleRepository;
import com.proyecto.integrador.Security.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    IRoleRepository roleRepository;
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
