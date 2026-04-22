package org.example.suenhator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.suenhator.utils.ViewLoader;

import java.net.URL;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.ViewLoader.cargarVista;

public class ReservasViewController implements Initializable {

    @FXML
    private Button botonAbrirFormularioNuevaReserva;

    @FXML
    private Button botonModificarReservaSeleccionada;

    @FXML
    private Button botonBuscarReserva;

    @FXML
    private Button botonConfirmarReserva;

    @FXML
    private Button botonMarcarReservaCompletada;

    @FXML
    private Button botonCancelarReserva;

    @FXML
    private Button botonCambiarEstadoPersonalizacion;

    @FXML
    private TextField campoTextoDniClienteReserva;

    @FXML
    private DatePicker selectorFiltroFechaReserva;

    @FXML
    private ListView<String> listaReservasFiltradas;

    @FXML
    private Label etiquetaTituloReservaSeleccionada;

    @FXML
    private Label etiquetaClienteReserva;

    @FXML
    private Label etiquetaPackReserva;

    @FXML
    private Label etiquetaSalaReserva;

    @FXML
    private Label etiquetaSupervisorReserva;

    @FXML
    private Label etiquetaFechaHoraReserva;

    @FXML
    private Label etiquetaDuracionReserva;

    @FXML
    private Label etiquetaEstadoReserva;

    @FXML
    private Label etiquetaEstadoPersonalizacionReserva;

    @FXML
    private Label etiquetaDescripcionPersonalizacionReserva;

    @FXML
    private ListView<String> listaAdjuntosPersonalizacionReserva;


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

        botonAbrirFormularioNuevaReserva.setOnAction(event ->
                cargarVista("formReserva-view.fxml", botonAbrirFormularioNuevaReserva, "Formulario de reserva"));

        botonModificarReservaSeleccionada.setOnAction(event -> {

        });

        botonBuscarReserva.setOnAction(event -> {

        });

        botonConfirmarReserva.setOnAction(event -> {

        });

        botonMarcarReservaCompletada.setOnAction(event -> {

        });

        botonCancelarReserva.setOnAction(event -> {

        });

        botonCambiarEstadoPersonalizacion.setOnAction(event -> {

        });
    }
}