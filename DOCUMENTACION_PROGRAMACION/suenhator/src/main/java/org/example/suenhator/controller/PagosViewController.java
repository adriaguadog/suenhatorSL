package org.example.suenhator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.suenhator.utils.ViewLoader;

import java.net.URL;
import java.util.ResourceBundle;

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
    private ListView<?> listaComprasPendientesCliente;

    @FXML
    private ComboBox<?> selectorEstadoPago;

    @FXML
    private DatePicker selectorFechaPago;

    @FXML
    private ComboBox<?> selectorMetodoPago;

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


    }
}