package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.enums.EstadoPago;
import model.enums.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Pago {

    private int idPago;
    private Compra compra;
    private LocalDate fechaPago;
    private double importe;
    private MetodoPago metodo;
    private EstadoPago estado;

    public Pago(Compra compra, LocalDate fechaPago, double importe, MetodoPago metodo, EstadoPago estado) {
        this.compra = compra;
        this.fechaPago = fechaPago;
        this.importe = importe;
        this.metodo = metodo;
        this.estado = estado;
    }
}