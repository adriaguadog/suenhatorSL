package org.example.suenhator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.example.suenhator.utils.ViewLoader;

import java.net.URL;
import java.util.ResourceBundle;

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
    private ComboBox<String> selectorPackCompra;

    @FXML
    private Spinner<Integer> selectorCantidadPackCompra;

    @FXML
    private ListView<String> listaLineasCompra;

    @FXML
    private Label etiquetaTotalCompra;

    @FXML
    private ListView<String> listaComprasCliente;

    private ViewLoader viewLoader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        viewLoader = new ViewLoader();
    }

    private void initGUI() {

    }

    private void actions() {

        botonRegistrarCompra.setOnAction(event -> {

        });

        botonBuscarClientePorDniCompra.setOnAction(event -> {

        });

        botonAnadirLineaCompra.setOnAction(event -> {

        });

        botonEliminarLineaCompra.setOnAction(event -> {

        });
    }
}
