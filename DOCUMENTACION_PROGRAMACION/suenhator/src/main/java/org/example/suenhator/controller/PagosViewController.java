package org.example.suenhator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.suenhator.dao.ClienteDAO;
import org.example.suenhator.dao.CompraDAO;
import org.example.suenhator.dao.PagoDAO;
import org.example.suenhator.model.Cliente;
import org.example.suenhator.model.Compra;
import org.example.suenhator.model.Pago;
import org.example.suenhator.model.enums.EstadoCompra;
import org.example.suenhator.model.enums.MetodoPago;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearInformation;
import static org.example.suenhator.utils.AlertCreation.crearWarning;

public class PagosViewController implements Initializable {

    @FXML
    private Button botonBuscarComprasPendientes;

    @FXML
    private Button botonGuardarPago;

    @FXML
    private TextField campoTextoDniClientePago;

    @FXML
    private TextField campoTextoImportePago;

    @FXML
    private Label etiquetaCompraSeleccionadaPago;

    @FXML
    private ListView<Compra> listViewComprasPendientesCliente;
    private ObservableList<Compra> listaComprasPendientesCliente;

    @FXML
    private ListView<Pago> listViewPagosCliente;
    private ObservableList<Pago> listaPagosCliente;

    @FXML
    private DatePicker selectorFechaPago;

    @FXML
    private ComboBox<MetodoPago> selectorMetodoPago;
    private ObservableList<MetodoPago> listaMetodosPago;

    private ClienteDAO clienteDAO;
    private CompraDAO compraDAO;
    private PagoDAO pagoDAO;

    private Cliente clienteSeleccionado;
    private Compra compraSeleccionada;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }


    private void instances() {
        clienteDAO = new ClienteDAO();
        compraDAO = new CompraDAO();
        pagoDAO = new PagoDAO();

        listaComprasPendientesCliente = FXCollections.observableArrayList();
        listaPagosCliente = FXCollections.observableArrayList();
        listaMetodosPago = FXCollections.observableArrayList(MetodoPago.values());
    }


    private void initGUI() {
        listViewComprasPendientesCliente.setItems(listaComprasPendientesCliente);
        listViewPagosCliente.setItems(listaPagosCliente);
        selectorMetodoPago.setItems(listaMetodosPago);

        etiquetaCompraSeleccionadaPago.setText("Selecciona una compra pendiente");
        selectorFechaPago.setValue(LocalDate.now());
    }


    private void actions() {

        botonBuscarComprasPendientes.setOnAction(event -> {
            limpiarVistaPago();

            if (campoTextoDniClientePago.getText() == null || campoTextoDniClientePago.getText().isBlank()) {
                crearWarning("DNI vacío", "Debes introducir un DNI");
                return;
            }

            clienteSeleccionado = clienteDAO.buscarPorDni(campoTextoDniClientePago.getText().trim());

            if (clienteSeleccionado == null) {
                crearWarning("Cliente no encontrado", "No existe ningún cliente con ese DNI");
                return;
            }

            cargarComprasPendientesCliente();
            cargarPagosCliente();
        });

        listViewComprasPendientesCliente.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            compraSeleccionada = newValue;
            actualizarCompraSeleccionadaEnVista();
        });

        botonGuardarPago.setOnAction(event -> {
            if (compraSeleccionada == null) {
                crearWarning("Sin compra", "Debes seleccionar una compra pendiente");
                return;
            }

            if (selectorFechaPago.getValue() == null) {
                crearWarning("Fecha vacía", "Debes seleccionar una fecha");
                return;
            }

            if (selectorMetodoPago.getSelectionModel().getSelectedItem() == null) {
                crearWarning("Método vacío", "Debes seleccionar un método de pago");
                return;
            }

            double importe;

            try {
                String textoImporte = campoTextoImportePago.getText();

                if (textoImporte == null || textoImporte.isBlank()) {
                    crearWarning("Importe incorrecto", "Debes introducir un importe válido");
                    return;
                }

                importe = Double.parseDouble(textoImporte.trim().replace(",", "."));

            } catch (Exception e) {
                crearWarning("Importe incorrecto", "Debes introducir un importe válido");
                return;
            }

            if (importe <= 0) {
                crearWarning("Importe incorrecto", "El importe debe ser mayor que 0");
                return;
            }

            Pago pago = pagoDAO.registrarPago(
                    compraSeleccionada,
                    importe,
                    selectorMetodoPago.getSelectionModel().getSelectedItem(),
                    selectorFechaPago.getValue()
            );

            if (pago == null) {
                crearWarning("Error", "No se pudo registrar el pago");
                return;
            }

            boolean estadoCambiado = compraDAO.cambiarEstadoCompra(
                    compraSeleccionada.getIdCompra(),
                    EstadoCompra.pagada
            );

            if (!estadoCambiado) {
                crearWarning("Aviso", "Se registró el pago, pero no se pudo actualizar el estado de la compra");
            }

            compraSeleccionada.setEstado(EstadoCompra.pagada);
            crearInformation("Pago registrado", "El pago se ha guardado correctamente");

            cargarComprasPendientesCliente();
            cargarPagosCliente();
            compraSeleccionada = null;
            listViewComprasPendientesCliente.getSelectionModel().clearSelection();
            etiquetaCompraSeleccionadaPago.setText("Selecciona una compra pendiente");
            campoTextoImportePago.clear();
        });
    }


    public void cargarCompra(Compra compra) {
        if (compra == null) {
            return;
        }

        clienteSeleccionado = compra.getCliente();
        compraSeleccionada = compra;

        if (clienteSeleccionado != null) {
            campoTextoDniClientePago.setText(clienteSeleccionado.getDni());
            cargarComprasPendientesCliente();
            cargarPagosCliente();
        }

        // Buscamos la compra en la lista ya cargada para seleccionarla
        boolean existe = false;
        for (Compra compraLista : listaComprasPendientesCliente) {
            if (compraLista.getIdCompra() == compraSeleccionada.getIdCompra()) {
                compraSeleccionada = compraLista;
                existe = true;
                break;
            }
        }

        if (!existe) {
            listaComprasPendientesCliente.add(0, compraSeleccionada);
        }

        listViewComprasPendientesCliente.getSelectionModel().select(compraSeleccionada);
        actualizarCompraSeleccionadaEnVista();
    }


    private void actualizarCompraSeleccionadaEnVista() {
        if (compraSeleccionada == null) {
            etiquetaCompraSeleccionadaPago.setText("Selecciona una compra pendiente");
            campoTextoImportePago.clear();
            return;
        }

        etiquetaCompraSeleccionadaPago.setText(
                "Compra del " + compraSeleccionada.getFecha()
                        + " - Total: " + String.format("%.2f", compraSeleccionada.getTotal()).replace(".", ",") + " €"
        );

        campoTextoImportePago.setText(
                String.format("%.2f", compraSeleccionada.getTotal()).replace(".", ",")
        );
    }


    private void cargarComprasPendientesCliente() {
        listaComprasPendientesCliente.clear();

        if (clienteSeleccionado == null) {
            return;
        }

        for (Compra compra : compraDAO.obtenerComprasPorCliente(clienteSeleccionado)) {
            if (compra.getEstado() == EstadoCompra.pendiente) {
                listaComprasPendientesCliente.add(compra);
            }
        }

        listViewComprasPendientesCliente.refresh();
    }


    private void cargarPagosCliente() {
        listaPagosCliente.clear();

        if (clienteSeleccionado == null) {
            return;
        }

        listaPagosCliente.addAll(pagoDAO.obtenerPagosPorCliente(clienteSeleccionado));
        listViewPagosCliente.refresh();
    }


    private void limpiarVistaPago() {
        listaComprasPendientesCliente.clear();
        listaPagosCliente.clear();
        compraSeleccionada = null;
        etiquetaCompraSeleccionadaPago.setText("Selecciona una compra pendiente");
        campoTextoImportePago.clear();
    }
}