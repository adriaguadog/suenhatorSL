package org.example.suenhator.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.suenhator.model.enums.MetodoPago;

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

    public Pago(Compra compra, LocalDate fechaPago, double importe, MetodoPago metodo) {
        this.compra = compra;
        this.fechaPago = fechaPago;
        this.importe = importe;
        this.metodo = metodo;
    }

    @Override
    public String toString() {
        return "Pago #" + idPago + " - " + fechaPago + " - " + metodo + " - " + String.format("%.2f", importe) + " €";
    }
}