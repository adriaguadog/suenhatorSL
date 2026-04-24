package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.enums.EstadoCompra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter

public class Compra {

    ArrayList<LineaCompra> lineaCompras;

    private int idCompra;
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
        return "Fecha: " + fecha
                + " | Total: " + String.format("%.2f", total) + " €"
                + " | Estado: " + estado;
    }

    public Compra() {
        lineaCompras=new ArrayList<>();
    }
}