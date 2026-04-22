package controller;

import model.*;
import model.enums.EstadoCompra;
import model.enums.EstadoPago;
import model.enums.MetodoPago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompraController {

    private ReservaController reservaController;
    private ArrayList<Compra> listaCompras;

    public CompraController() {
        listaCompras = new ArrayList<>();
    }

    public Compra registrarCompra(Cliente cliente) {
        if (cliente == null) {
            System.out.println("cliente no existente");
            return null;
        }

        Compra compra = new Compra(cliente, LocalDate.now(), 0.0, EstadoCompra.PENDIENTE);
        listaCompras.add(compra);

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

        if (!listaCompras.contains(compra)) {
            System.out.println("compra no encontrada");
            return false;
        }

        compra.setEstado(estado);
        System.out.println("estado de la compra cambiado correctamente");
        return true;
    }

    public boolean cancelarCompra(Compra compra) {
        System.out.println("compra cancelada correctamente");
        return cambiarEstadoCompra(compra, EstadoCompra.CANCELADA);
    }

    public List<Compra> consultarComprasDeCliente(String dni) {
        if (dni.isBlank()) {
            System.out.println("cliente no existente");
        }

        List<Compra> comprasCliente = listaCompras.stream()
                .filter(compra -> compra.getCliente().getDni().equalsIgnoreCase(dni))
                .collect(Collectors.toList());

        if (comprasCliente.isEmpty()) {
            System.out.println("no se encontraron compras para ese cliente");
        } else {
            return comprasCliente;
        }
        return null;
    }
}
