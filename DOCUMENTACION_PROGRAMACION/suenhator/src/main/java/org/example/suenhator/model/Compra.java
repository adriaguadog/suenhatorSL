package org.example.suenhator.model;

import lombok.Getter;
import lombok.Setter;
import org.example.suenhator.model.enums.EstadoCompra;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter

public class Compra {

    ArrayList<LineaCompra> lineaCompras;

    private int idCompra;
    private int idReserva;
    private Cliente cliente;
    private LocalDate fecha;
    private double total;
    private EstadoCompra estado;

    public Compra(Cliente cliente, LocalDate fecha, double total, EstadoCompra estado) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        lineaCompras=new ArrayList<>();
    }

    @Override
    public String toString() {
        String reservaTexto = idReserva > 0 ? "Reserva #" + idReserva + " - " : "";
        return reservaTexto + "Compra #" + idCompra + " - " + estado + " - " + String.format("%.2f", total) + " €";
    }

    public Compra() {
        lineaCompras=new ArrayList<>();
    }
}