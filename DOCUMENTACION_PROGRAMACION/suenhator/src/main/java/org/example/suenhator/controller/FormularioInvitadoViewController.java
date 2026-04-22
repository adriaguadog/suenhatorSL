package org.example.suenhator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FormularioInvitadoViewController implements Initializable {

    @FXML
    private Button botonAnadirInvitadoAReserva;

    @FXML
    private Button botonEliminarInvitadoSeleccionado;

    @FXML
    private Button botonGuardarInvitado;

    @FXML
    private Button botonLimpiarFormularioInvitado;

    @FXML
    private Button botonNuevoInvitado;

    @FXML
    private TextField campoApellidosInvitado;

    @FXML
    private TextField campoDniInvitado;

    @FXML
    private TextField campoEmailInvitado;

    @FXML
    private TextField campoNombreInvitado;

    @FXML
    private TextField campoTelefonoInvitado;

    @FXML
    private ListView<?> listaInvitadosPreparados;

    @FXML
    private DatePicker selectorFechaNacimientoInvitado;

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
        botonLimpiarFormularioInvitado.setOnAction(event -> {

        });

        botonGuardarInvitado.setOnAction(event -> {

        });

        botonNuevoInvitado.setOnAction(event -> {

        });

        botonAnadirInvitadoAReserva.setOnAction(event -> {

        });

        botonEliminarInvitadoSeleccionado.setOnAction(event -> {

        });
    }
}