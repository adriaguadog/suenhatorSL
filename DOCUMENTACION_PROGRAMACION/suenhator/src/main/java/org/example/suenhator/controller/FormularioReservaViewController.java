package org.example.suenhator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.suenhator.utils.ViewLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class FormularioReservaViewController implements Initializable {

    @FXML
    private TextArea areaDescripcionPersonalizacionReservaFormulario;

    @FXML
    private Button botonAbrirFormularioInvitados;

    @FXML
    private Button botonAdjuntarArchivoReserva;

    @FXML
    private Button botonAnadirInvitadoExistenteReserva;

    @FXML
    private Button botonBuscarClienteReservaFormulario;

    @FXML
    private Button botonEliminarArchivoReserva;

    @FXML
    private Button botonGuardarReserva;

    @FXML
    private Button botonLimpiarFormularioReserva;

    @FXML
    private Button botonQuitarInvitadoReserva;

    @FXML
    private TextField campoHoraReservaFormulario;

    @FXML
    private TextField campoTextoBusquedaInvitadoReserva;

    @FXML
    private TextField campoTextoDniClienteReservaFormulario;

    @FXML
    private TextField campoVideoReferenciaReservaFormulario;

    @FXML
    private CheckBox checkReservaConfirmadaFormulario;

    @FXML
    private Label etiquetaClienteEncontradoReserva;

    @FXML
    private ListView<?> listaArchivosAdjuntosReservaFormulario;

    @FXML
    private ListView<?> listaInvitadosReservaFormulario;

    @FXML
    private ComboBox<?> selectorEstadoPersonalizacionReservaFormulario;

    @FXML
    private ComboBox<?> selectorEstadoReservaFormulario;

    @FXML
    private DatePicker selectorFechaReservaFormulario;

    @FXML
    private ComboBox<?> selectorPackReservaFormulario;

    @FXML
    private ComboBox<?> selectorSalaReservaFormulario;

    @FXML
    private ComboBox<?> selectorSupervisorReservaFormulario;

    @FXML
    private Spinner<?> spinnerNumeroInvitadosReserva;

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
        botonLimpiarFormularioReserva.setOnAction(event -> {

        });

        botonGuardarReserva.setOnAction(event -> {

        });

        botonBuscarClienteReservaFormulario.setOnAction(event -> {

        });

        botonAbrirFormularioInvitados.setOnAction(event ->
                viewLoader.cargarVista("formInvitado-view.fxml", botonAbrirFormularioInvitados, "Formulario de invitados"));

        botonAnadirInvitadoExistenteReserva.setOnAction(event -> {

        });

        botonQuitarInvitadoReserva.setOnAction(event -> {

        });

        botonAdjuntarArchivoReserva.setOnAction(event -> {

        });

        botonEliminarArchivoReserva.setOnAction(event -> {

        });
    }
}