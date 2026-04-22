package controller;

import model.Cliente;
import model.Compra;
import model.LineaCompra;
import model.Pack;
import model.Pago;
import model.enums.EstadoCompra;
import model.enums.EstadoPago;
import model.enums.MetodoPago;
import org.example.suenhator.data.Dataset;

import java.time.LocalDate;

public class CompraController {

    private ReservaController reservaController;

    public CompraController() {
    }

    public Compra registrarCompra(Cliente cliente) {
        if (cliente == null) {
            System.out.println("cliente no existente");
            return null;
        }

        Compra compra = new Compra(cliente, LocalDate.now(), 0.0, EstadoCompra.PENDIENTE);
        Dataset.listaCompras.add(compra);

        System.out.println("compra registrada correctamente");
        return compra;
    }

    public boolean anadirLineaCompra(Compra compra, Pack pack, int cantidad) {
        if (compra == null || pack == null) {
            System.out.println("debe seleccionar un pack y una compra existentes");
        } else if (cantidad <= 0) {
            System.out.println("la cantidad debe ser mayor que 0");
        } else {
            double precioUnitario = pack.getPrecio();
            double subtotal = precioUnitario * cantidad;
            LineaCompra lineaCompra = new LineaCompra(compra, pack, cantidad, precioUnitario, subtotal);
            compra.getLineaCompras().add(lineaCompra);
            System.out.println("anhadido correctamente");
            calcularTotal(compra);
            return true;
        }
        return false;
    }

    private double calcularTotal(Compra compra) {
        double total = 0.0;
        for (LineaCompra linea : compra.getLineaCompras()) {
            total += linea.getSubtotal();
        }
        compra.setTotal(total);
        return total;
    }

    public Pago registrarPago(Compra compra, double total, MetodoPago metodo, LocalDate fecha) {
        if (compra == null) {
            System.out.println("la compra no existe");
        } else {
            Pago pago = new Pago(compra, fecha, compra.getTotal(), metodo, EstadoPago.PENDIENTE);
            Dataset.listaPagos.add(pago);
            cambiarEstadoCompra(compra, EstadoCompra.PAGADA);
            return pago;
        }
        return null;
    }

    private boolean cambiarEstadoCompra(Compra compra, EstadoCompra estado) {
        if (compra == null || estado == null) {
            System.out.println("no se pudo cambiar el estado");
            return false;
        }

        if (!Dataset.listaCompras.contains(compra)) {
            System.out.println("compra no encontrada");
            return false;
        }

        compra.setEstado(estado);
        System.out.println("estado cambiado correctamente");
        return true;
    }
}