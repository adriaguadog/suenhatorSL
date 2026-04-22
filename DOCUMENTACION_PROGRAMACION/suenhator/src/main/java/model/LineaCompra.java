package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
}