package com.proyecto.integrador.Security.Controller;

import com.proyecto.integrador.Controller.Mensaje;
import com.proyecto.integrador.Security.Dto.JwtDto;
import com.proyecto.integrador.Security.Dto.LoginUsuario;
import com.proyecto.integrador.Security.Dto.NuevoUsuario;
import com.proyecto.integrador.Security.Entitiy.Rol;
import com.proyecto.integrador.Security.Entitiy.Usuario;
import com.proyecto.integrador.Security.Enums.RolNombre;
import com.proyecto.integrador.Security.Jwt.JwtProvider;
import com.proyecto.integrador.Security.Service.RolService;
import com.proyecto.integrador.Security.Service.UsuarioService;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
//@CrossOrigin
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://porfoliofrontend.web.app")
public class AuthControllere {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RolService rolService;
    @Autowired
    JwtProvider jwtProvider;
    
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
        //verificamos los campos//
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos o mail inválido"), HttpStatus.BAD_REQUEST);
        
        if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new Mensaje("Ese nombre ya existe"),HttpStatus.BAD_REQUEST);
        
        if(usuarioService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new Mensaje("Ese email ya existe"),HttpStatus.BAD_REQUEST);        
        //Acá creamos al nuestro nuevo usuario//
        Usuario usuario = new Usuario (nuevoUsuario.getNombre(),
                                       nuevoUsuario.getNombreUsuario(),
                                       nuevoUsuario.getEmail(),
                                       passwordEncoder.encode(nuevoUsuario.getPassword()));
        //Le agregamos los roles a los usuarios//
        Set<Rol> roles = new HashSet<>();
        //por defecto, el nuevo rol será de usuario//
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
        //si contiene admin, le ponemos como rol Admin//
        if(nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        //le pasamos a la clase usuario//
        usuario.setRoles(roles);
        //Acá guiardamos el usuario//
        usuarioService.save(usuario);
    return new ResponseEntity(new Mensaje("Usuario guardado"), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtDto> login (@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
       if(bindingResult.hasErrors()){
           return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
       }
       Authentication authentication =
               authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(),
                                                                                          loginUsuario.getPassword()));
       SecurityContextHolder.getContext().setAuthentication(authentication);
       String jwt = jwtProvider.generateToken(authentication);
       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       
       JwtDto jwtDto = new JwtDto (jwt, userDetails.getUsername(), userDetails.getAuthorities());
       return new ResponseEntity(jwtDto,HttpStatus.OK);
    }
}
