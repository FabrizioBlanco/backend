package com.proyecto.integrador.Controller;

import com.proyecto.integrador.Dto.DtoPersona;
import com.proyecto.integrador.Entity.Persona;
import com.proyecto.integrador.Service.ImpPersonaService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/persona")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://proyectoargprogfrontend.web.app")//sirve para que acepte las peticiones desde esa página
public class PersonaController {
    @Autowired
    ImpPersonaService ipersonaService; 
    
    @GetMapping("/lista")
    public ResponseEntity<List<Persona>> list(){
        List<Persona> list= ipersonaService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody DtoPersona dtoPersona){
        if(!ipersonaService.existsById(id)){
            return new ResponseEntity(new Mensaje("No existe el ID"), HttpStatus.NOT_FOUND);
        }
        if(ipersonaService.existsByNombre(dtoPersona.getNombre()) && ipersonaService.getByNombre(dtoPersona.getNombre()).get().getId() != id){
            return new ResponseEntity(new Mensaje("Ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(dtoPersona.getNombre())){
            return new ResponseEntity(new Mensaje("El campo NOMBRE no puede estar vacio"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(dtoPersona.getApellido())){
            return new ResponseEntity(new Mensaje("El campo APELLIDO no puede estar vacio"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(dtoPersona.getDescripcion())){
            return new ResponseEntity(new Mensaje("El campo DESCRIPCION no puede estar vacio"), HttpStatus.BAD_REQUEST);
        }
        
        
        Persona persona = ipersonaService.getOne(id).get();
        
        persona.setNombre(dtoPersona.getNombre());
        persona.setApellido(dtoPersona.getApellido());
        persona.setDescripcion(dtoPersona.getDescripcion());
        persona.setImg(dtoPersona.getImg());
        
        
        ipersonaService.save(persona);
        
        return new ResponseEntity(new Mensaje("Persona actualizada"), HttpStatus.OK);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Persona> getById(@PathVariable("id")int id){
        if(!ipersonaService.existsById(id)){
            return new ResponseEntity(new Mensaje("No existe el ID"), HttpStatus.BAD_REQUEST);
        }
        
        Persona persona = ipersonaService.getOne(id).get();
        return new ResponseEntity(persona, HttpStatus.OK);
    }
    
    
    //Solo para Postman
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DtoPersona dtoPersona){      
        if(StringUtils.isBlank(dtoPersona.getNombre()))
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if(ipersonaService.existsByNombre(dtoPersona.getNombre()))
            return new ResponseEntity(new Mensaje("Esa experiencia existe"), HttpStatus.BAD_REQUEST);
        
        Persona experiencia = new Persona(dtoPersona.getNombre(), dtoPersona.getDescripcion(), dtoPersona.getApellido(), dtoPersona.getImg());
        ipersonaService.save(experiencia);
        
        return new ResponseEntity(new Mensaje("Persona agregada"), HttpStatus.OK);
    }

//    @GetMapping("/traer/perfil")
//    public Persona findPersona(){
//        return ipersonaService.findPersona((long)1);
//    }
}