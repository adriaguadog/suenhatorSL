package org.example.suenhator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineaCompra {

    private Compra compra;
    private Pack pack;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    @Override
    public String toString() {
        return pack.getNombre() + " x" + cantidad + " - " + String.format("%.2f", subtotal) + " €";
    }
}