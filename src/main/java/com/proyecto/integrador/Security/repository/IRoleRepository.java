package com.proyecto.integrador.Security.repository;

import com.proyecto.integrador.Security.model.Role;
import com.proyecto.integrador.Security.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
