package org.example.suenhator.controller;

import controller.ClienteController;
import controller.CompraController;
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
import model.Cliente;
import model.Compra;
import model.Pago;
import model.enums.EstadoCompra;
import model.enums.EstadoPago;
import model.enums.MetodoPago;
import org.example.suenhator.data.Dataset;
import org.example.suenhator.utils.ViewLoader;

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
    private ListView<String> listViewComprasPendientesCliente;
    private ObservableList<String> listaComprasPendientesCliente;

    @FXML
    private ComboBox<EstadoPago> selectorEstadoPago;
    private ObservableList<EstadoPago> listaEstadosPago;

    @FXML
    private DatePicker selectorFechaPago;

    @FXML
    private ComboBox<MetodoPago> selectorMetodoPago;
    private ObservableList<MetodoPago> listaMetodosPago;

    private ViewLoader viewLoader;
    private ClienteController clienteController;
    private CompraController compraController;

    private Cliente clienteSeleccionado;
    private Compra compraSeleccionada;
    private ObservableList<Compra> listaComprasPendientesReales;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        //inicializo utilidades y controladores
        viewLoader = new ViewLoader();
        clienteController = new ClienteController();
        compraController = new CompraController();

        //inicializo las listas asociadas a los controles
        listaComprasPendientesCliente = FXCollections.observableArrayList();
        listaEstadosPago = FXCollections.observableArrayList(EstadoPago.values());
        listaMetodosPago = FXCollections.observableArrayList(MetodoPago.values());
        listaComprasPendientesReales = FXCollections.observableArrayList();
    }

    private void initGUI() {
        //asocio las listas a los controles
        listViewComprasPendientesCliente.setItems(listaComprasPendientesCliente);
        selectorEstadoPago.setItems(listaEstadosPago);
        selectorMetodoPago.setItems(listaMetodosPago);

        //dejo valores iniciales
        etiquetaCompraSeleccionadaPago.setText("Selecciona una compra pendiente");
        selectorFechaPago.setValue(LocalDate.now());
        selectorEstadoPago.getSelectionModel().select(EstadoPago.PENDIENTE);
    }

    private void actions() {

        botonBuscarComprasPendientes.setOnAction(event -> {
            //limpio datos anteriores
            listaComprasPendientesCliente.clear();
            listaComprasPendientesReales.clear();
            compraSeleccionada = null;
            etiquetaCompraSeleccionadaPago.setText("Selecciona una compra pendiente");
            campoTextoImportePago.clear();

            //compruebo si el dni esta vacio
            if (campoTextoDniClientePago.getText() == null || campoTextoDniClientePago.getText().isBlank()) {
                crearWarning("DNI vacío", "Debes introducir un DNI");
                return;
            }

            //busco el cliente
            clienteSeleccionado = clienteController.buscarPorDni(campoTextoDniClientePago.getText());

            //compruebo si existe
            if (clienteSeleccionado == null) {
                crearWarning("Cliente no encontrado", "No existe ningún cliente con ese DNI");
                return;
            }

            //busco compras pendientes del cliente
            for (Compra compra : Dataset.listaCompras) {
                if (compra.getCliente() != null
                        && compra.getCliente().getDni() != null
                        && compra.getCliente().getDni().equalsIgnoreCase(clienteSeleccionado.getDni())
                        && compra.getEstado() == EstadoCompra.PENDIENTE) {

                    listaComprasPendientesReales.add(compra);

                    String textoCompra = "Fecha: " + compra.getFecha()
                            + " | Total: " + String.format("%.2f", compra.getTotal()) + " €"
                            + " | Estado: " + compra.getEstado();

                    listaComprasPendientesCliente.add(textoCompra);
                }
            }

            //compruebo si hay resultados
            if (listaComprasPendientesCliente.isEmpty()) {
                crearWarning("Sin compras pendientes", "El cliente no tiene compras pendientes");
            }
        });

        listViewComprasPendientesCliente.setOnMouseClicked(event -> {
            //cojo la posicion seleccionada
            int indiceSeleccionado = listViewComprasPendientesCliente.getSelectionModel().getSelectedIndex();

            //compruebo si hay selección
            if (indiceSeleccionado < 0) {
                return;
            }

            //recupero la compra real
            compraSeleccionada = listaComprasPendientesReales.get(indiceSeleccionado);

            //muestro la compra seleccionada
            etiquetaCompraSeleccionadaPago.setText(
                    "Compra del " + compraSeleccionada.getFecha()
                            + " - Total: " + String.format("%.2f", compraSeleccionada.getTotal()) + " €"
            );

            campoTextoImportePago.setText(String.format("%.2f", compraSeleccionada.getTotal()));
        });

        botonGuardarPago.setOnAction(event -> {
            //compruebo si hay compra seleccionada
            if (compraSeleccionada == null) {
                crearWarning("Sin compra", "Debes seleccionar una compra pendiente");
                return;
            }

            //compruebo fecha
            if (selectorFechaPago.getValue() == null) {
                crearWarning("Fecha vacía", "Debes seleccionar una fecha");
                return;
            }

            //compruebo metodo
            if (selectorMetodoPago.getSelectionModel().getSelectedItem() == null) {
                crearWarning("Método vacío", "Debes seleccionar un método de pago");
                return;
            }

            //compruebo importe
            double importe;
            try {
                importe = Double.parseDouble(campoTextoImportePago.getText());
            } catch (Exception e) {
                crearWarning("Importe incorrecto", "Debes introducir un importe válido");
                return;
            }

            //compruebo que el importe sea positivo
            if (importe <= 0) {
                crearWarning("Importe incorrecto", "El importe debe ser mayor que 0");
                return;
            }

            //registro el pago
            Pago pago = compraController.registrarPago(
                    compraSeleccionada,
                    importe,
                    selectorMetodoPago.getSelectionModel().getSelectedItem(),
                    selectorFechaPago.getValue()
            );

            //compruebo si se ha creado
            if (pago == null) {
                crearWarning("Error", "No se pudo registrar el pago");
                return;
            }

            //si quieres respetar el selector de estado, lo actualizo despues
            if (selectorEstadoPago.getSelectionModel().getSelectedItem() != null) {
                pago.setEstado(selectorEstadoPago.getSelectionModel().getSelectedItem());
            }

            crearInformation("Pago registrado", "El pago se ha guardado correctamente");

            //refresco la lista de compras pendientes
            listaComprasPendientesCliente.clear();
            listaComprasPendientesReales.clear();
            compraSeleccionada = null;
            etiquetaCompraSeleccionadaPago.setText("Selecciona una compra pendiente");
            campoTextoImportePago.clear();

            if (clienteSeleccionado != null) {
                for (Compra compra : Dataset.listaCompras) {
                    if (compra.getCliente() != null
                            && compra.getCliente().getDni() != null
                            && compra.getCliente().getDni().equalsIgnoreCase(clienteSeleccionado.getDni())
                            && compra.getEstado() == EstadoCompra.PENDIENTE) {

                        listaComprasPendientesReales.add(compra);

                        String textoCompra = "Fecha: " + compra.getFecha()
                                + " | Total: " + String.format("%.2f", compra.getTotal()) + " €"
                                + " | Estado: " + compra.getEstado();

                        listaComprasPendientesCliente.add(textoCompra);
                    }
                }
            }
        });
    }
}