
package com.proyecto.integrador.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter //con esto me ahorro los getters y setters en el código
@Entity //el archivo contiene entidades
public class Persona {
    @Id //primaryKey
    @GeneratedValue(strategy = GenerationType.IDENTITY) //automaticamente se le agrega un número
    private int id;
    @NotNull
    @Size(min = 1, max = 50, message = "Longitud inválida")
    private String nombre;
    @NotNull
    @Size(min = 1, max = 50, message = "Longitud inválida")
    private String apellido;
    @NotNull
    @Size(min = 1, message = "Longitud inválida")
    private String descripcion;
    
    private String img;

    public Persona() {
    }

    public Persona(String nombre, String apellido, String descripcion, String img) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.descripcion = descripcion;
        this.img = img;
    }
    
    
    
}
