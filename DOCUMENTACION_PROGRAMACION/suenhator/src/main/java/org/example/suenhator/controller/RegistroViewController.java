package org.example.suenhator.controller;

import controller.ClienteController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Cliente;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearInformation;
import static org.example.suenhator.utils.AlertCreation.crearWarning;
import static org.example.suenhator.utils.ViewLoader.cargarVista;

public class RegistroViewController implements Initializable {

    @FXML
    private Button botonRegistrarNuevoCliente;

    @FXML
    private TextField campoTextoApellidosCliente;

    @FXML
    private TextField campoTextoCorreoCliente;

    @FXML
    private Button botonAtras;

    @FXML
    private TextField campoTextoDniCliente;

    @FXML
    private TextField campoTextoNombreCliente;

    @FXML
    private TextField campoTextoTelefonoCliente;

    @FXML
    private DatePicker selectorFechaNacimientoCliente;

    private Cliente clienteSeleccionado;
    private ClienteController clienteController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        clienteController = new ClienteController();
    }

    private void initGUI() {

    }

    private void actions() {
        botonAtras.setOnAction(event -> {
            cargarVista("main-view.fxml", botonAtras, "Gestor de clientes");
        });

        botonRegistrarNuevoCliente.setOnAction(event -> {
            //algo esta vacio?
            if (campoTextoNombreCliente.getText().isEmpty()
                    || campoTextoApellidosCliente.getText().isEmpty()
                    || campoTextoDniCliente.getText().isEmpty()
                    || campoTextoTelefonoCliente.getText().isEmpty()
                    || campoTextoCorreoCliente.getText().isEmpty()
                    || selectorFechaNacimientoCliente.getValue() == null) {
                crearWarning("Formulario incompleto", "Debes rellenar todos los campos");
                return;
            }

            //modificarDatos si se ha cargado la vista con un cliente
            if (clienteSeleccionado != null) {
                //creo un cliente nuevo con el contenido actual de los campos para sustituir al anterior
                Cliente clienteModificado = clienteController.modificarDatos(
                        new Cliente(
                                campoTextoNombreCliente.getText(),
                                campoTextoApellidosCliente.getText(),
                                campoTextoDniCliente.getText(),
                                campoTextoTelefonoCliente.getText(),
                                campoTextoCorreoCliente.getText(),
                                clienteSeleccionado.getFechaAlta(),
                                selectorFechaNacimientoCliente.getValue()
                        )
                );
                //cliente esta?
                if (clienteModificado != null) {
                    crearInformation("Acción completada", "Cliente modificado correctamente");
                    cargarVista("main-view.fxml", botonRegistrarNuevoCliente, "Gestor de clientes");
                } else {
                    crearWarning("Error", "No se ha podido modificar el cliente");
                }

            } else {
                //creo el cliente con el contenido de los campos
                Cliente clienteRegistrado = clienteController.darDeAlta(
                        new Cliente(
                                campoTextoNombreCliente.getText(),
                                campoTextoApellidosCliente.getText(),
                                campoTextoDniCliente.getText(),
                                campoTextoTelefonoCliente.getText(),
                                campoTextoCorreoCliente.getText(),
                                LocalDate.now(),
                                selectorFechaNacimientoCliente.getValue()
                        )
                );

                if (clienteRegistrado != null) {
                    crearInformation("Acción completada", "Cliente registrado correctamente");
                    limpiarFormulario();
                } else {
                    crearWarning("Error", "No se ha podido registrar el cliente");
                }
            }
        });
    }

    //metodo para cargar el cliente seleccionado si se accede mediante el btn modificar datos
    public void cargarCliente(Cliente cliente) {
        //si no se recibe cliente no hago nada
        if (cliente == null) {
            return;
        }

        this.clienteSeleccionado = cliente;

        //si se accede con un cliente cargado es para modificar datos
        //pongo los datos actuales
        campoTextoNombreCliente.setText(cliente.getNombre());
        campoTextoApellidosCliente.setText(cliente.getApellidos());
        campoTextoDniCliente.setText(cliente.getDni());
        campoTextoTelefonoCliente.setText(cliente.getTelefono());
        campoTextoCorreoCliente.setText(cliente.getEmail());
        selectorFechaNacimientoCliente.setValue(cliente.getFechaNac());

        //cambio el texto del boton para que sea para modificar los datos del cliente
        botonRegistrarNuevoCliente.setText("Guardar cambios");

        //bloqueo el dni para que no cambie la referencia del cliente a modificar
        campoTextoDniCliente.setEditable(false);
    }

    //metodo para limpiar el formulario despues del alta
    private void limpiarFormulario() {
        campoTextoNombreCliente.clear();
        campoTextoApellidosCliente.clear();
        campoTextoDniCliente.clear();
        campoTextoTelefonoCliente.clear();
        campoTextoCorreoCliente.clear();
        selectorFechaNacimientoCliente.setValue(null);
    }
}