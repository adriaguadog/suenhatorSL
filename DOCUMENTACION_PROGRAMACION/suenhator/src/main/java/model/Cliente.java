package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Cliente {

    private int idCliente;
    private String nombre;
    private String apellidos;
    private String dni;
    private String telefono;
    private String email;
    private LocalDate fechaAlta;
    private LocalDate fechaNac;

    @Override
    public String toString() {
        return (
                "idCliente=" + idCliente +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", fechaNac=" + fechaNac
                );
    }

        public Cliente(String nombre, String apellidos, String dni, String telefono,
                String email, LocalDate fechaAlta, LocalDate fechaNac) {
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.dni = dni;
            this.telefono = telefono;
            this.email = email;
            this.fechaAlta = fechaAlta;
            this.fechaNac = fechaNac;
        }

}