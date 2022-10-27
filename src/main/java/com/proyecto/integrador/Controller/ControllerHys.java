/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.integrador.Controller;

import com.proyecto.integrador.Dto.DtoHys;
import com.proyecto.integrador.Entity.hys;
import com.proyecto.integrador.Service.Shys;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hysskill")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://porfoliofrontend.web.app")
public class ControllerHys {
    @Autowired
    Shys sHyS;
    //LISTO
    @GetMapping("/lista")
    public ResponseEntity<List<hys>> list(){
        List<hys> list= sHyS.lista();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    //LISTO
    @GetMapping("/detail/{id}")
    public ResponseEntity<hys> getById(@PathVariable("id")int id){
        if(!sHyS.existsById(id)){
            return new ResponseEntity(new Mensaje("No existe el ID"), HttpStatus.BAD_REQUEST);
        }
        
        hys educacion = sHyS.getOne(id).get();
        return new ResponseEntity(educacion, HttpStatus.OK);
    }
    //LISTO
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        if(!sHyS.existsById(id)){
            return new ResponseEntity(new Mensaje("No existe el ID"), HttpStatus.NOT_FOUND);
        }
        sHyS.delete(id);
        return new ResponseEntity(new Mensaje("Skill eliminada"), HttpStatus.OK);
    }
    //LISTO
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DtoHys dtoHyS){
        if(StringUtils.isBlank(dtoHyS.getNombre())){
            return new ResponseEntity(new Mensaje("La skill es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if(sHyS.existsByNombre(dtoHyS.getNombre())){
            return new ResponseEntity(new Mensaje("Ese skill ya existe"), HttpStatus.BAD_REQUEST);
        }
        
        hys Skills = new hys(dtoHyS.getNombre(), dtoHyS.getPorcentaje());
        sHyS.save(Skills);
        return new ResponseEntity(new Mensaje("Skill creada"), HttpStatus.OK);
                
    }
    //LISTO
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody DtoHys dtoHyS){
        if(!sHyS.existsById(id)){
            return new ResponseEntity(new Mensaje("No existe el ID"), HttpStatus.NOT_FOUND);
        }
        if(sHyS.existsByNombre(dtoHyS.getNombre()) && sHyS.getByNombre(dtoHyS.getNombre()).get().getId() != id){
            return new ResponseEntity(new Mensaje("Ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(dtoHyS.getNombre())){
            return new ResponseEntity(new Mensaje("El campo no puede estar vacio"), HttpStatus.BAD_REQUEST);
        }
        
        hys skills = sHyS.getOne(id).get();
        
        skills.setNombre(dtoHyS.getNombre());
        skills.setPorcentaje(dtoHyS.getPorcentaje());
        
        sHyS.save(skills);
        
        return new ResponseEntity(new Mensaje("Educacion actualizada"), HttpStatus.OK);
    }
}
