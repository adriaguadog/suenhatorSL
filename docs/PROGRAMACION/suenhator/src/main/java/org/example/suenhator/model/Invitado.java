package org.example.suenhator.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Invitado {

    private int idInvitado;
    private String nombre;
    private String apellidos;
    private String dni;
    private String telefono;
    private String email;
    private LocalDate fechaNac;

    public Invitado(String nombre, String apellidos, String dni, String telefono,
                    String email, LocalDate fechaNac) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.fechaNac = fechaNac;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " - " + dni;
    }
}