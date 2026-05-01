package org.example.suenhator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import org.example.suenhator.dao.SalaDAO;
import org.example.suenhator.dao.SupervisorDAO;
import org.example.suenhator.model.Sala;
import org.example.suenhator.model.Supervisor;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearInformation;
import static org.example.suenhator.utils.AlertCreation.crearWarning;

public class RecursosViewController implements Initializable {

    @FXML
    private ListView<Sala> listViewSalas;
    private ObservableList<Sala> listaSalas;

    @FXML
    private ListView<Supervisor> listViewSupervisores;
    private ObservableList<Supervisor> listaSupervisores;

    @FXML
    private TabPane panelPestanasRecursos;

    @FXML
    private Button botonAnadirSala;

    @FXML
    private Button botonEliminarSala;

    @FXML
    private Button botonAnadirSupervisor;

    @FXML
    private Button botonEliminarSupervisor;

    private SalaDAO salaDAO;
    private SupervisorDAO supervisorDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        listaSupervisores = FXCollections.observableArrayList();
        listaSalas = FXCollections.observableArrayList();
        salaDAO = new SalaDAO();
        supervisorDAO = new SupervisorDAO();
    }

    private void initGUI() {
        listViewSupervisores.setItems(listaSupervisores);
        listViewSalas.setItems(listaSalas);

        cargarSalas();
        cargarSupervisores();
    }

    private void actions() {
        botonAnadirSala.setOnAction(event -> anadirSala());
        botonEliminarSala.setOnAction(event -> eliminarSalaSeleccionada());

        botonAnadirSupervisor.setOnAction(event -> anadirSupervisor());
        botonEliminarSupervisor.setOnAction(event -> eliminarSupervisorSeleccionado());
    }

    private void cargarSalas() {
        listaSalas.setAll(salaDAO.obtenerSalas());
    }

    private void cargarSupervisores() {
        listaSupervisores.setAll(supervisorDAO.obtenerSupervisores());
    }

    private void anadirSala() {
        TextInputDialog dialogNombre = new TextInputDialog();
        dialogNombre.setTitle("Nueva sala");
        dialogNombre.setHeaderText("Crear sala");
        dialogNombre.setContentText("Nombre:");

        Optional<String> resultadoNombre = dialogNombre.showAndWait();

        if (resultadoNombre.isEmpty() || resultadoNombre.get().isBlank()) {
            crearWarning("Datos incompletos", "Debes introducir el nombre de la sala");
            return;
        }

        if (salaDAO.buscarSalaPorNombre(resultadoNombre.get().trim()) != null) {
            crearWarning("Sala repetida", "Ya existe una sala con ese nombre");
            return;
        }

        TextInputDialog dialogCapacidad = new TextInputDialog();
        dialogCapacidad.setTitle("Nueva sala");
        dialogCapacidad.setHeaderText("Crear sala");
        dialogCapacidad.setContentText("Capacidad:");

        Optional<String> resultadoCapacidad = dialogCapacidad.showAndWait();

        if (resultadoCapacidad.isEmpty() || resultadoCapacidad.get().isBlank()) {
            crearWarning("Datos incompletos", "Debes introducir la capacidad");
            return;
        }

        int capacidad;

        try {
            capacidad = Integer.parseInt(resultadoCapacidad.get().trim());
        } catch (Exception e) {
            crearWarning("Capacidad incorrecta", "Debes introducir un número válido");
            return;
        }

        if (capacidad <= 0) {
            crearWarning("Capacidad incorrecta", "La capacidad debe ser mayor que 0");
            return;
        }

        Sala sala = salaDAO.registrarSala(
                resultadoNombre.get().trim(),
                capacidad
        );

        if (sala == null) {
            crearWarning("Error", "No se pudo crear la sala");
            return;
        }

        cargarSalas();
        listViewSalas.getSelectionModel().select(sala);
        crearInformation("Sala creada", "La sala se ha registrado correctamente");
    }

    private void eliminarSalaSeleccionada() {
        Sala salaSeleccionada = listViewSalas.getSelectionModel().getSelectedItem();

        if (salaSeleccionada == null) {
            crearWarning("Sin selección", "Debes seleccionar una sala");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar sala");
        alerta.setHeaderText("Confirmar eliminación");
        alerta.setContentText("¿Deseas eliminar la sala seleccionada?");

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {
            return;
        }

        boolean eliminada = salaDAO.eliminarSala(salaSeleccionada);

        if (!eliminada) {
            crearWarning("Error", "No se pudo eliminar la sala");
            return;
        }

        cargarSalas();
        crearInformation("Sala eliminada", "La sala se ha eliminado correctamente");
    }

    private void anadirSupervisor() {
        TextInputDialog dialogNombre = new TextInputDialog();
        dialogNombre.setTitle("Nuevo supervisor");
        dialogNombre.setHeaderText("Crear supervisor");
        dialogNombre.setContentText("Nombre:");

        Optional<String> resultadoNombre = dialogNombre.showAndWait();

        if (resultadoNombre.isEmpty() || resultadoNombre.get().isBlank()) {
            crearWarning("Datos incompletos", "Debes introducir el nombre");
            return;
        }

        TextInputDialog dialogApellidos = new TextInputDialog();
        dialogApellidos.setTitle("Nuevo supervisor");
        dialogApellidos.setHeaderText("Crear supervisor");
        dialogApellidos.setContentText("Apellidos:");

        Optional<String> resultadoApellidos = dialogApellidos.showAndWait();

        if (resultadoApellidos.isEmpty() || resultadoApellidos.get().isBlank()) {
            crearWarning("Datos incompletos", "Debes introducir los apellidos");
            return;
        }

        TextInputDialog dialogDni = new TextInputDialog();
        dialogDni.setTitle("Nuevo supervisor");
        dialogDni.setHeaderText("Crear supervisor");
        dialogDni.setContentText("DNI:");

        Optional<String> resultadoDni = dialogDni.showAndWait();

        if (resultadoDni.isEmpty() || resultadoDni.get().isBlank()) {
            crearWarning("Datos incompletos", "Debes introducir el DNI");
            return;
        }

        if (supervisorDAO.buscarSupervisorPorDni(resultadoDni.get().trim()) != null) {
            crearWarning("DNI repetido", "Ya existe un supervisor con ese DNI");
            return;
        }

        TextInputDialog dialogTelefono = new TextInputDialog();
        dialogTelefono.setTitle("Nuevo supervisor");
        dialogTelefono.setHeaderText("Crear supervisor");
        dialogTelefono.setContentText("Teléfono:");

        Optional<String> resultadoTelefono = dialogTelefono.showAndWait();

        TextInputDialog dialogEmail = new TextInputDialog();
        dialogEmail.setTitle("Nuevo supervisor");
        dialogEmail.setHeaderText("Crear supervisor");
        dialogEmail.setContentText("Email:");

        Optional<String> resultadoEmail = dialogEmail.showAndWait();

        Supervisor supervisor = supervisorDAO.registrarSupervisor(
                resultadoNombre.get().trim(),
                resultadoApellidos.get().trim(),
                resultadoDni.get().trim(),
                resultadoTelefono.orElse("").trim(),
                resultadoEmail.orElse("").trim()
        );

        if (supervisor == null) {
            crearWarning("Error", "No se pudo crear el supervisor");
            return;
        }

        cargarSupervisores();
        listViewSupervisores.getSelectionModel().select(supervisor);
        crearInformation("Supervisor creado", "El supervisor se ha registrado correctamente");
    }

    private void eliminarSupervisorSeleccionado() {
        Supervisor supervisorSeleccionado = listViewSupervisores.getSelectionModel().getSelectedItem();

        if (supervisorSeleccionado == null) {
            crearWarning("Sin selección", "Debes seleccionar un supervisor");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar supervisor");
        alerta.setHeaderText("Confirmar eliminación");
        alerta.setContentText("¿Deseas eliminar al supervisor seleccionado?");

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {
            return;
        }

        boolean eliminado = supervisorDAO.eliminarSupervisor(supervisorSeleccionado);

        if (!eliminado) {
            crearWarning("Error", "No se pudo eliminar el supervisor");
            return;
        }

        cargarSupervisores();
        crearInformation("Supervisor eliminado", "El supervisor se ha eliminado correctamente");
    }
}