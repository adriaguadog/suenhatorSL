package org.example.suenhator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.example.suenhator.utils.ViewLoader;

import java.net.URL;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.ViewLoader.cargarVista;

public class MainViewController implements Initializable {

    @FXML
    private Button botonAbrirVistaClientes;

    @FXML
    private Button botonAbrirVistaPacks;

    @FXML
    private Button botonAbrirVistaRecursos;

    @FXML
    private Button botonAbrirVistaReservas;

    @FXML
    private Button botonAbrirVistaCompras;

    @FXML
    private Button botonAbrirVistaPagos;

    @FXML
    private StackPane panelContenedorContenido;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        ViewLoader.setPanelContenedorContenido(panelContenedorContenido);
    }

    private void initGUI() {

    }

    private void actions() {
        botonAbrirVistaClientes.setOnAction(event -> cargarVista("clientes-view.fxml", "Gestor de clientes"));
        botonAbrirVistaPacks.setOnAction(event -> cargarVista("packs-view.fxml", "Catálogo de packs"));
        botonAbrirVistaRecursos.setOnAction(event -> cargarVista("recursos-view.fxml", "Salas y supervisores"));
        botonAbrirVistaReservas.setOnAction(event -> cargarVista("reservas-view.fxml", "Gestor de reservas"));
        botonAbrirVistaCompras.setOnAction(event -> cargarVista("compras-view.fxml", "Gestor de compras"));
        botonAbrirVistaPagos.setOnAction(event -> cargarVista("pagos-view.fxml", "Gestor de pagos"));
    }
}
