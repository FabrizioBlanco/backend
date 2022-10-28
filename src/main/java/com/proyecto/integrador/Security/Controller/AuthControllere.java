package com.proyecto.integrador.Security.Controller;

import com.proyecto.integrador.Controller.Mensaje;
import com.proyecto.integrador.Security.Dto.JwtDto;
import com.proyecto.integrador.Security.Dto.LoginUsuario;
import com.proyecto.integrador.Security.Dto.NuevoUsuario;
import com.proyecto.integrador.Security.Entitiy.Rol;
import com.proyecto.integrador.Security.Entitiy.Usuario;
import com.proyecto.integrador.Security.Entitiy.UsuarioPrincipal;
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
@RequestMapping("/permisos")
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
    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody NuevoUsuario nuevoUsuario){
        if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario())){
            return new ResponseEntity<>(new Mensaje("The username is existed"), HttpStatus.OK);
        }
        if(usuarioService.existsByEmail(nuevoUsuario.getEmail())){
            return new ResponseEntity<>(new Mensaje("The email is existed"), HttpStatus.OK);
        }
        Usuario users = new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(), passwordEncoder.encode(nuevoUsuario.getPassword()));
        Set<String> strRoles = nuevoUsuario.getRoles();
        Set<Rol> roles = new HashSet<>();
        strRoles.forEach(role ->{
            switch (role){
                case "admin":
                    Rol adminRole = rolService.getByRolNombre(RolNombre.ROLE_ADMIN).orElseThrow( ()-> new RuntimeException("Role not found"));
                    roles.add(adminRole);
                    break;
                default:
                    Rol userRole = rolService.getByRolNombre(RolNombre.ROLE_USER).orElseThrow( ()-> new RuntimeException("Role not found"));
                    roles.add(userRole);
            }
        });
        users.setRoles(roles);
        usuarioService.save(users);
        return new ResponseEntity<>(new Mensaje("Create success!"), HttpStatus.OK);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUsuario loginUsuario){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        UsuarioPrincipal userPrinciple = (UsuarioPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtDto(token, userPrinciple.getNombre(), userPrinciple.getAuthorities()));
    }
//    @PostMapping("/crear")
//    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
//        //verificamos los campos//
//        if(bindingResult.hasErrors())
//            return new ResponseEntity(new Mensaje("campos mal puestos o mail inválido"), HttpStatus.BAD_REQUEST);
//        
//        if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
//            return new ResponseEntity(new Mensaje("Ese nombre ya existe"),HttpStatus.BAD_REQUEST);
//        
//        if(usuarioService.existsByEmail(nuevoUsuario.getEmail()))
//            return new ResponseEntity(new Mensaje("Ese email ya existe"),HttpStatus.BAD_REQUEST);        
//        //Acá creamos al nuestro nuevo usuario//
//        Usuario usuario = new Usuario (nuevoUsuario.getNombre(),
//                                       nuevoUsuario.getNombreUsuario(),
//                                       nuevoUsuario.getEmail(),
//                                       passwordEncoder.encode(nuevoUsuario.getPassword()));
//        //Le agregamos los roles a los usuarios//
//        Set<Rol> roles = new HashSet<>();
//        //por defecto, el nuevo rol será de usuario//
//        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
//        //si contiene admin, le ponemos como rol Admin//
//        if(nuevoUsuario.getRoles().contains("admin"))
//            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
//        //le pasamos a la clase usuario//
//        usuario.setRoles(roles);
//        //Acá guiardamos el usuario//
//        usuarioService.save(usuario);
//    return new ResponseEntity(new Mensaje("Usuario guardado"), HttpStatus.CREATED);
//    }
//    @PostMapping("/entrar")
//    public ResponseEntity<JwtDto> login (@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
//       if(bindingResult.hasErrors()){
//           return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
//       }
//       Authentication authentication =
//               authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(),
//                                                                                          loginUsuario.getPassword()));
//       SecurityContextHolder.getContext().setAuthentication(authentication);
//       String jwt = jwtProvider.generateToken(authentication);
//       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//       
//       JwtDto jwtDto = new JwtDto (jwt, userDetails.getUsername(), userDetails.getAuthorities());
//       return new ResponseEntity(jwtDto,HttpStatus.OK);
//    }
}
