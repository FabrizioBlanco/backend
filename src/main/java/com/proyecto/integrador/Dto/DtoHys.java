
package com.proyecto.integrador.Dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DtoHys {
    @NotBlank
    private String nombre;
    @NotBlank
    private int porcentaje;

    public DtoHys() {
    }

    public DtoHys(String nombre, int porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }
    
    
}
