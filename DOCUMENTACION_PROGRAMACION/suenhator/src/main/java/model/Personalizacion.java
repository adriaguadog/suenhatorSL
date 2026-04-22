package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.enums.EstadoPersonalizacion;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Personalizacion {

    private int idPersonalizacion;
    private Reserva reserva;
    private String videoRef;
    private String descripcion;
    private LocalDate fechaSolicitud;
    private LocalDate fechaAprobacion;
    private EstadoPersonalizacion estado;

    public Personalizacion(Reserva reserva, String videoRef, String descripcion,
                           LocalDate fechaSolicitud, LocalDate fechaAprobacion, EstadoPersonalizacion estado) {
        this.reserva = reserva;
        this.videoRef = videoRef;
        this.descripcion = descripcion;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaAprobacion = fechaAprobacion;
        this.estado = estado;
    }
}