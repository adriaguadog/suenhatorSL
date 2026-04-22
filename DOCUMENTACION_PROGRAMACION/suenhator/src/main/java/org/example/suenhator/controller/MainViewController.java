package org.example.suenhator.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearError;

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

    }

    private void initGUI() {

    }

    private void actions() {
        botonAbrirVistaClientes.setOnAction(event -> cargarVistaEnPanel("clientes-view.fxml"));
        botonAbrirVistaPacks.setOnAction(event -> cargarVistaEnPanel("packs-view.fxml"));
        botonAbrirVistaRecursos.setOnAction(event -> cargarVistaEnPanel("recursos-view.fxml"));
        botonAbrirVistaReservas.setOnAction(event -> cargarVistaEnPanel("reservas-view.fxml"));
        botonAbrirVistaCompras.setOnAction(event -> cargarVistaEnPanel("compras-view.fxml"));
        botonAbrirVistaPagos.setOnAction(event -> cargarVistaEnPanel("pagos-view.fxml"));
    }

    private void cargarVistaEnPanel(String nombreVista) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/suenhator/" + nombreVista));
            Node vista = loader.load();
            panelContenedorContenido.getChildren().setAll(vista);
        } catch (IOException e) {
        crearError("Error", "No se ha encontrado el recurso");
        }
    }
}
