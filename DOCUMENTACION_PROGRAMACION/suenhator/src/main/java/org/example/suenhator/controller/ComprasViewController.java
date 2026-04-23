package org.example.suenhator.controller;

import controller.ClienteController;
import controller.CompraController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import model.Cliente;
import model.Compra;
import model.LineaCompra;
import model.Pack;
import org.example.suenhator.data.Dataset;
import org.example.suenhator.utils.ViewLoader;

import java.net.URL;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearInformation;
import static org.example.suenhator.utils.AlertCreation.crearWarning;

public class ComprasViewController implements Initializable {

    @FXML
    private Button botonRegistrarCompra;

    @FXML
    private Button botonBuscarClientePorDniCompra;

    @FXML
    private Button botonAnadirLineaCompra;

    @FXML
    private Button botonEliminarLineaCompra;

    @FXML
    private TextField campoDniClienteCompra;

    @FXML
    private Label etiquetaClienteCompraSeleccionado;

    @FXML
    private ComboBox<Pack> selectorPackCompra;
    private ObservableList<Pack> listaPacksCompra;

    @FXML
    private Spinner<Integer> selectorCantidadPackCompra;

    @FXML
    private ListView<String> listViewLineasCompra;
    private ObservableList<String> listaLineasCompra;

    @FXML
    private Label etiquetaTotalCompra;

    @FXML
    private ListView<String> listViewComprasCliente;
    private ObservableList<String> listaComprasCliente;

    private ClienteController clienteController;
    private CompraController compraController;

    private Cliente clienteSeleccionado;
    private Compra compraActual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        clienteController = new ClienteController();
        compraController = new CompraController();

        //inicializo las listas asociadas a los controles
        listaPacksCompra = FXCollections.observableArrayList(Dataset.listaPacks);
        listaLineasCompra = FXCollections.observableArrayList();
        listaComprasCliente = FXCollections.observableArrayList();
    }

    private void initGUI() {
        //asocio listas a los controles
        selectorPackCompra.setItems(listaPacksCompra);
        listViewLineasCompra.setItems(listaLineasCompra);
        listViewComprasCliente.setItems(listaComprasCliente);

        //configuro el spinner de cantidad
        selectorCantidadPackCompra.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 1, 1));

        //dejo etiquetas limpias
        etiquetaClienteCompraSeleccionado.setText("Ningún cliente seleccionado");
        etiquetaTotalCompra.setText("Total: 0,00 €");
    }

    private void actions() {

        botonRegistrarCompra.setOnAction(event -> {
            //compruebo que haya cliente seleccionado
            if (clienteSeleccionado == null) {
                crearWarning("Sin cliente", "Debes buscar un cliente antes de registrar la compra");
                return;
            }

            //compruebo que no haya ya una compra en curso
            if (compraActual != null) {
                crearWarning("Compra ya iniciada", "Ya hay una compra en curso para este cliente");
                return;
            }

            //creo la compra
            compraActual = compraController.registrarCompra(clienteSeleccionado);

            //compruebo que se haya creado bien
            if (compraActual == null) {
                crearWarning("Error", "No se pudo iniciar la compra");
                return;
            }

            //limpio lineas y actualizo la vista
            listaLineasCompra.clear();
            actualizarTotalCompra();
            actualizarHistorialComprasCliente();
            crearInformation("Compra iniciada", "Ya puedes añadir líneas a la compra");
        });


        botonBuscarClientePorDniCompra.setOnAction(event -> {
            //compruebo si el dni esta vacio
            if (campoDniClienteCompra.getText() == null || campoDniClienteCompra.getText().isBlank()) {
                crearWarning("DNI vacío", "Debes introducir un DNI");
                return;
            }

            //busco el cliente
            clienteSeleccionado = clienteController.buscarPorDni(campoDniClienteCompra.getText());

            //no encontrado?
            if (clienteSeleccionado == null) {
                etiquetaClienteCompraSeleccionado.setText("Ningún cliente seleccionado");
                listaComprasCliente.clear();
                compraActual = null;
                listaLineasCompra.clear();
                actualizarTotalCompra();
                crearWarning("Cliente no encontrado", "No existe ningún cliente con ese DNI");
                return;
            }

            //si existe lo muestro
            etiquetaClienteCompraSeleccionado.setText(clienteSeleccionado.getNombre() + " " + clienteSeleccionado.getApellidos());

            //dejo la compra actual vacia hasta que se pulse registrar compra
            compraActual = null;
            listaLineasCompra.clear();
            actualizarTotalCompra();
            actualizarHistorialComprasCliente();
        });


        botonAnadirLineaCompra.setOnAction(event -> {
            //compruebo compra activa
            if (compraActual == null) {
                crearWarning("Compra no iniciada", "Debes buscar primero un cliente");
                return;
            }

            //compruebo pack seleccionado
            Pack packSeleccionado = selectorPackCompra.getSelectionModel().getSelectedItem();
            if (packSeleccionado == null) {
                crearWarning("Sin pack", "Debes seleccionar un pack");
                return;
            }

            int cantidad = selectorCantidadPackCompra.getValue();

            //intento añadir la linea
            boolean anadida = compraController.anadirLineaCompra(compraActual, packSeleccionado, cantidad);

            if (!anadida) {
                crearWarning("Error", "No se pudo añadir la línea de compra");
                return;
            }

            //refresco la lista visible
            actualizarLineasCompra();
            actualizarTotalCompra();
        });

        botonEliminarLineaCompra.setOnAction(event -> {
            //compruebo compra activa
            if (compraActual == null) {
                crearWarning("Compra no iniciada", "No hay compra activa");
                return;
            }

            //cojo la posicion seleccionada
            int indiceSeleccionado = listViewLineasCompra.getSelectionModel().getSelectedIndex();

            //compruebo si hay selección
            if (indiceSeleccionado < 0) {
                crearWarning("Sin selección", "Debes seleccionar una línea");
                return;
            }

            //elimino la linea de la compra
            compraActual.getLineaCompras().remove(indiceSeleccionado);

            //recalculo el total manualmente
            double total = 0.0;
            for (LineaCompra linea : compraActual.getLineaCompras()) {
                total += linea.getSubtotal();
            }
            compraActual.setTotal(total);

            //refresco la vista
            actualizarLineasCompra();
            actualizarTotalCompra();
        });
    }

    private void actualizarLineasCompra() {
        //limpio la lista visible
        listaLineasCompra.clear();

        //compruebo que haya compra activa
        if (compraActual == null) {
            return;
        }

        //recorro lineas y las paso a texto
        for (LineaCompra linea : compraActual.getLineaCompras()) {
            String textoLinea = linea.getPack().getNombre()
                    + " x" + linea.getCantidad()
                    + " - " + String.format("%.2f", linea.getSubtotal()) + " €";

            listaLineasCompra.add(textoLinea);
        }
    }

    private void actualizarTotalCompra() {
        //si no hay compra muestro 0
        if (compraActual == null) {
            etiquetaTotalCompra.setText("Total: 0,00 €");
            return;
        }

        etiquetaTotalCompra.setText("Total: " + String.format("%.2f", compraActual.getTotal()) + " €");
    }

    private void actualizarHistorialComprasCliente() {
        //limpio la lista visible
        listaComprasCliente.clear();

        //compruebo que haya cliente seleccionado
        if (clienteSeleccionado == null) {
            return;
        }

        //recorro las compras del cliente
        for (Compra compra : Dataset.listaCompras) {
            if (compra.getCliente() != null
                    && compra.getCliente().getDni() != null
                    && compra.getCliente().getDni().equalsIgnoreCase(clienteSeleccionado.getDni())) {

                String textoCompra = "Fecha: " + compra.getFecha()
                        + " | Total: " + String.format("%.2f", compra.getTotal()) + " €"
                        + " | Estado: " + compra.getEstado();

                listaComprasCliente.add(textoCompra);
            }
        }
    }
}
