
package com.proyecto.integrador.Security.Repository;

import com.proyecto.integrador.Security.Entitiy.Rol;
import com.proyecto.integrador.Security.Enums.RolNombre;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RolRepository extends JpaRepository<Rol,Integer> {
    Optional<Rol> findByRolNombre (RolNombre rolNombre);
}
