/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.integrador.Service;

import com.proyecto.integrador.Entity.hys;
import com.proyecto.integrador.Repository.Rhys;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class Shys {
    @Autowired
    Rhys rHyS;
    
    public List<hys> lista(){
        return rHyS.findAll();
    }
    public Optional<hys> getOne(int id){
         return rHyS.findById(id);
     }
     
     public Optional<hys> getByNombre(String nombre){
         return rHyS.findByNombre(nombre);
     }
     
     public void save(hys hardAndSoftSkill){
         rHyS.save(hardAndSoftSkill);
     }
     
     public void delete(int id){
         rHyS.deleteById(id);
     }
     
     public boolean existsById(int id){
         return rHyS.existsById(id);
     }
     
     public boolean existsByNombre(String nombre){
         return rHyS.existsByNombre(nombre);
     }
    
    
}
