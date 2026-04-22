package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.enums.EstadoReserva;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
public class Reserva {

    private int idReserva;
    private Cliente cliente;
    private Sala sala;
    private Pack pack;
    private Supervisor supervisor;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoReserva estado;
    private boolean esConfirmado;

    public Reserva(Cliente cliente, Sala sala, Pack pack, Supervisor supervisor,
                   LocalDate fecha, LocalTime hora) {
        this.cliente = cliente;
        this.sala = sala;
        this.pack = pack;
        this.supervisor = supervisor;
        this.fecha = fecha;
        this.hora = hora;
        this.esConfirmado=false;
        this.estado=EstadoReserva.PENDIENTE;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", cliente=" + cliente +
                ", sala=" + sala +
                ", pack=" + pack +
                ", supervisor=" + supervisor +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", estado='" + estado + '\'' +
                ", esConfirmado=" + esConfirmado +
                '}';
    }
}