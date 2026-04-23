package org.example.suenhator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Invitado;
import org.example.suenhator.data.Dataset;

import java.net.URL;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearInformation;
import static org.example.suenhator.utils.AlertCreation.crearWarning;

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
    private ListView<Invitado> listViewInvitadosPreparados;
    private ObservableList<Invitado> listaInvitadosPreparados;

    @FXML
    private DatePicker selectorFechaNacimientoInvitado;

    private Invitado invitadoSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        //inicializo la lista visible asociada al listview
        listaInvitadosPreparados = FXCollections.observableArrayList();
        listaInvitadosPreparados.addAll(Dataset.listaInvitados);
    }

    private void initGUI() {
        //asocio la lista visible al listview
        listViewInvitadosPreparados.setItems(listaInvitadosPreparados);

        //dejo el formulario limpio al iniciar
        limpiarFormulario();
    }

    private void actions() {
        botonLimpiarFormularioInvitado.setOnAction(event -> {
            limpiarFormulario();
        });

        botonGuardarInvitado.setOnAction(event -> {
            //compruebo campos obligatorios
            if (campoNombreInvitado.getText() == null || campoNombreInvitado.getText().isBlank()
                    || campoApellidosInvitado.getText() == null || campoApellidosInvitado.getText().isBlank()
                    || campoDniInvitado.getText() == null || campoDniInvitado.getText().isBlank()) {

                crearWarning("Datos incompletos", "Nombre, apellidos y DNI son obligatorios");
                return;
            }

            //compruebo si el dni ya existe en la lista preparada
            boolean dniRepetido = listaInvitadosPreparados.stream()
                    .anyMatch(invitado -> invitado.getDni() != null
                            && invitado.getDni().equalsIgnoreCase(campoDniInvitado.getText())
                            && invitado != invitadoSeleccionado);

            if (dniRepetido) {
                crearWarning("DNI repetido", "Ya hay un invitado preparado con ese DNI");
                return;
            }

            //si no hay invitado seleccionado creo uno nuevo
            if (invitadoSeleccionado == null) {
                Invitado invitadoNuevo = new Invitado(
                        campoNombreInvitado.getText(),
                        campoApellidosInvitado.getText(),
                        campoDniInvitado.getText(),
                        campoTelefonoInvitado.getText(),
                        campoEmailInvitado.getText(),
                        selectorFechaNacimientoInvitado.getValue()
                );

                listaInvitadosPreparados.add(invitadoNuevo);
                crearInformation("Invitado guardado", "El invitado se ha añadido correctamente");
            } else {
                //si ya habia uno seleccionado lo modifico
                invitadoSeleccionado.setNombre(campoNombreInvitado.getText());
                invitadoSeleccionado.setApellidos(campoApellidosInvitado.getText());
                invitadoSeleccionado.setDni(campoDniInvitado.getText());
                invitadoSeleccionado.setTelefono(campoTelefonoInvitado.getText());
                invitadoSeleccionado.setEmail(campoEmailInvitado.getText());
                invitadoSeleccionado.setFechaNac(selectorFechaNacimientoInvitado.getValue());

                //fuerzo refresco visual
                listViewInvitadosPreparados.refresh();
                crearInformation("Invitado modificado", "El invitado se ha modificado correctamente");
            }

            limpiarFormulario();
        });

        botonNuevoInvitado.setOnAction(event -> {
            limpiarFormulario();
        });

        botonAnadirInvitadoAReserva.setOnAction(event -> {
            //compruebo si hay invitados preparados
            if (listaInvitadosPreparados.isEmpty()) {
                crearWarning("Sin invitados", "No hay invitados preparados para añadir");
                return;
            }

            //guardo los invitados en el dataset compartido
            Dataset.listaInvitados.setAll(listaInvitadosPreparados);
            crearInformation("Invitados añadidos", "Los invitados se han añadido a la reserva");
        });

        botonEliminarInvitadoSeleccionado.setOnAction(event -> {
            //cojo el invitado seleccionado en la lista
            Invitado invitado = listViewInvitadosPreparados.getSelectionModel().getSelectedItem();

            //compruebo que haya selección
            if (invitado == null) {
                crearWarning("Sin selección", "Debes seleccionar un invitado de la lista");
                return;
            }

            //elimino el invitado de la lista visible
            listaInvitadosPreparados.remove(invitado);

            //si era el que estaba cargado en el formulario, limpio
            if (invitadoSeleccionado == invitado) {
                limpiarFormulario();
            }

            crearInformation("Invitado eliminado", "El invitado se ha eliminado correctamente");
        });

        listViewInvitadosPreparados.setOnMouseClicked(event -> {
            //cojo el invitado seleccionado
            Invitado invitado = listViewInvitadosPreparados.getSelectionModel().getSelectedItem();

            //compruebo que no sea nulo
            if (invitado == null) {
                return;
            }

            //guardo el invitado seleccionado y lo cargo en el formulario
            invitadoSeleccionado = invitado;
            cargarInvitado(invitadoSeleccionado);
        });
    }

    private void limpiarFormulario() {
        //quito la referencia al invitado seleccionado
        invitadoSeleccionado = null;

        //limpio los campos
        campoNombreInvitado.clear();
        campoApellidosInvitado.clear();
        campoDniInvitado.clear();
        campoTelefonoInvitado.clear();
        campoEmailInvitado.clear();
        selectorFechaNacimientoInvitado.setValue(null);
    }

    private void cargarInvitado(Invitado invitado) {
        //compruebo que el invitado exista
        if (invitado == null) {
            return;
        }

        //cargo sus datos en el formulario
        campoNombreInvitado.setText(invitado.getNombre());
        campoApellidosInvitado.setText(invitado.getApellidos());
        campoDniInvitado.setText(invitado.getDni());
        campoTelefonoInvitado.setText(invitado.getTelefono());
        campoEmailInvitado.setText(invitado.getEmail());
        selectorFechaNacimientoInvitado.setValue(invitado.getFechaNac());
    }
}