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
import model.enums.EstadoCompra;
import org.example.suenhator.data.Dataset;

import java.net.URL;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearWarning;

public class ComprasViewController implements Initializable {

    @FXML
    private Button botonRegistrarCompra;

    @FXML
    private Button botonBuscarClientePorDni;

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
    private ListView<LineaCompra> listViewLineasCompra;
    private ObservableList<LineaCompra> listaLineasCompra;

    @FXML
    private Label etiquetaTotalCompra;

    @FXML
    private ListView<Compra> listViewComprasCliente;
    private ObservableList<Compra> listaComprasCliente;

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
        //asocio listas
        selectorPackCompra.setItems(listaPacksCompra);
        listViewLineasCompra.setItems(listaLineasCompra);
        listViewComprasCliente.setItems(listaComprasCliente);

        //configuro el spinner
        selectorCantidadPackCompra.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 1, 1));

        // etiquetas limpias
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

            //creo la compra
            compraActual = compraController.registrarCompra(clienteSeleccionado);
            //compruebo que se haya creado bien
            if (compraActual == null) {
                crearWarning("Error", "No se pudo iniciar la compra");
                return;
            }

            //limpio lineas y actualizo vista
            listaLineasCompra.clear();
            actualizarTotalCompra();
            actualizarComprasCliente();
            listViewComprasCliente.getSelectionModel().select(compraActual);
            actualizarLineasCompra();
        });


        botonBuscarClientePorDni.setOnAction(event -> {
            //compruebo si el dni esta vacio
            if (campoDniClienteCompra.getText() == null || campoDniClienteCompra.getText().isBlank()) {
                crearWarning("DNI vacío", "Debes introducir un DNI");
                return;
            }
            //busco al cliente
            clienteSeleccionado = clienteController.buscarPorDni(campoDniClienteCompra.getText());
            //no encontrado?
            if (clienteSeleccionado == null) {
                //limpio el detalle
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

            //dejo la compra actual vacia hasta que se seleccione o cree una
            compraActual = null;
            listaLineasCompra.clear();
            actualizarTotalCompra();
            actualizarComprasCliente();
        });


        botonAnadirLineaCompra.setOnAction(event -> {
            //compruebo compra activa
            if (compraActual == null) {
                crearWarning("Compra no iniciada", "Debes crear o seleccionar una compra");
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
            actualizarComprasCliente();
        });

        botonEliminarLineaCompra.setOnAction(event -> {
            //compruebo compra activa
            if (compraActual == null) {
                crearWarning("Compra no iniciada", "No hay compra activa");
                return;
            }

            LineaCompra lineaSeleccionada = listViewLineasCompra.getSelectionModel().getSelectedItem();

            //compruebo si hay selección
            if (lineaSeleccionada == null) {
                crearWarning("Sin selección", "Debes seleccionar una línea");
                return;
            }

            boolean eliminada = compraController.eliminarLineaCompra(compraActual, lineaSeleccionada);

            if (!eliminada) {
                crearWarning("Error", "No se pudo eliminar la línea de compra");
                return;
            }

            //refresco la vista
            actualizarLineasCompra();
            actualizarTotalCompra();
            actualizarComprasCliente();
        });

        listViewComprasCliente.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            compraActual = newValue;
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
        listaLineasCompra.setAll(compraActual.getLineaCompras());
    }

    private void actualizarTotalCompra() {
        //si no hay compra muestro 0
        if (compraActual == null) {
            etiquetaTotalCompra.setText("Total: 0,00 €");
            return;
        }
        etiquetaTotalCompra.setText("Total: " + String.format("%.2f", compraActual.getTotal()) + " €");
    }

    private void actualizarComprasCliente() {
        //limpio la lista visible
        listaComprasCliente.clear();
        //compruebo que haya cliente seleccionado
        if (clienteSeleccionado == null) {
            return;
        }
        listaComprasCliente.setAll(compraController.obtenerComprasPorCliente(clienteSeleccionado));
    }

    //quiero que los botones para modificar la compra se inhabiliten si esta pagada
    private void actualizarEstadoBotones() {
        if (compraActual == null) {
            botonAnadirLineaCompra.setDisable(true);
            botonEliminarLineaCompra.setDisable(true);
            selectorPackCompra.setVisible(true);
            selectorPackCompra.setManaged(true);
            selectorCantidadPackCompra.setVisible(true);
            selectorCantidadPackCompra.setManaged(true);
            return;
        }

        //tambien si esta cancelada los inhabilito
        boolean compraBloqueada = compraActual.getEstado() == EstadoCompra.PAGADA
                || compraActual.getEstado() == EstadoCompra.CANCELADA;
        botonAnadirLineaCompra.setDisable(compraBloqueada);
        botonEliminarLineaCompra.setDisable(compraBloqueada);

        selectorPackCompra.setVisible(!compraBloqueada);
        selectorPackCompra.setManaged(!compraBloqueada);

        selectorCantidadPackCompra.setVisible(!compraBloqueada);
        selectorCantidadPackCompra.setManaged(!compraBloqueada);

        botonAnadirLineaCompra.setVisible(!compraBloqueada);
        botonAnadirLineaCompra.setManaged(!compraBloqueada);

        botonEliminarLineaCompra.setVisible(!compraBloqueada);
        botonEliminarLineaCompra.setManaged(!compraBloqueada);
    }
}