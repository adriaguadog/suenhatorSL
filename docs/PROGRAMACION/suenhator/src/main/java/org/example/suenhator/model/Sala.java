package org.example.suenhator.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Sala {

    private int idSala;
    private String nombre;
    private int capacidad;

    public Sala(String nombre, int capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return nombre + " (" + capacidad + " personas)";
    }
}