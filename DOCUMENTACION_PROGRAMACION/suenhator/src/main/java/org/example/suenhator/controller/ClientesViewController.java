package org.example.suenhator.controller;

import controller.ClienteController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Cliente;
import org.example.suenhator.HelloApplication;
import org.example.suenhator.data.Dataset;
import org.example.suenhator.utils.ViewLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearInformation;
import static org.example.suenhator.utils.AlertCreation.crearWarning;
import static org.example.suenhator.utils.ViewLoader.cargarVista;

public class ClientesViewController implements Initializable {

    private ClienteController clienteController;

    @FXML
    private Button botonBuscarClientePorDni;

    @FXML
    private Button botonDarDeBajaCliente;

    @FXML
    private Button botonModificarCliente;

    @FXML
    private Button botonNuevoCliente;

    @FXML
    private TextField campoTextoBusquedaClientes;

    @FXML
    private ListView<Cliente> listViewClientes;

    @FXML
    private Label etiquetaNombre;

    @FXML
    private Label etiquetaDni;

    @FXML
    private Label etiquetaTelefono;

    @FXML
    private Label etiquetaEmail;

    @FXML
    private Label etiquetaFechaNac;

    //lista asociada
    private ObservableList<Cliente> listaClientes;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        clienteController = new ClienteController();
        listaClientes = FXCollections.observableArrayList();
    }

    private void initGUI() {
        //asocio la listview a una lista observable para no modificar directamente la del dataset
        listViewClientes.setItems(listaClientes);
        //cargo todos los clientes del dataset al iniciar
        cargarClientes(Dataset.listaClientes);
        limpiarDetalle();
    }

    private void actions() {

        botonNuevoCliente.setOnAction(event -> {
            cargarVista("registro-view.fxml", "Registro cliente");
        });

        botonModificarCliente.setOnAction(event -> {
            Cliente clienteSeleccionado = listViewClientes.getSelectionModel().getSelectedItem();

            if (clienteSeleccionado == null) {
                crearWarning("Sin selección", "Debes seleccionar un cliente para modificarlo");
                return;
            }

            etiquetaNombre.setText(clienteSeleccionado.getNombre() + " " + clienteSeleccionado.getApellidos());
            etiquetaDni.setText("DNI: " + clienteSeleccionado.getDni());
            etiquetaTelefono.setText("Teléfono: " + clienteSeleccionado.getTelefono());
            etiquetaEmail.setText("Correo: " + clienteSeleccionado.getEmail());
            etiquetaFechaNac.setText("Fecha de nacimiento: " + clienteSeleccionado.getFechaNac());

            try {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("registro-view.fxml"));
                Parent vista = loader.load();

                RegistroViewController controller = loader.getController();
                controller.cargarCliente(clienteSeleccionado);

                ViewLoader.getPanelContenedorContenido().getChildren().setAll(vista);

            } catch (IOException e) {
                crearWarning("Error", "No se ha podido abrir el formulario de modificación");
            }
        });


        botonDarDeBajaCliente.setOnAction(event -> {
            Cliente clienteSeleccionado = listViewClientes.getSelectionModel().getSelectedItem();
            if (clienteController.darDeBaja(clienteSeleccionado)) {
                crearInformation("Accion completada", "Cliente eliminado correctamente");
                //recargo la lista mostrada con la lista actual del dataset
                cargarClientes(Dataset.listaClientes);
                limpiarDetalle();
            }
        });

        botonBuscarClientePorDni.setOnAction(event -> {
            //cojo el texto
            String dni = campoTextoBusquedaClientes.getText();

            //dni vacio?
            if (dni == null || dni.isBlank()) {
                cargarClientes(Dataset.listaClientes);
                limpiarDetalle();
                crearWarning("Error", "El dni no puede estar vacio");
                return;
            }

            Cliente clienteEncontrado = clienteController.buscarPorDni(dni);

            if (clienteEncontrado != null) {
                //muestro solo el cliente encontrado en la lista visible
                listaClientes.setAll(clienteEncontrado);
                listViewClientes.getSelectionModel().select(clienteEncontrado);
            } else {
                //si no se encuentra, dejo la lista vacia y aviso
                listaClientes.clear();
                limpiarDetalle();
                crearWarning("Cliente no encontrado", "No se ha encontrado ningun usuario asociado al dni");
            }
        });
    }

    //metodos
    private void cargarClientes(ObservableList<Cliente> clientes) {
        //cargo en la lista visible los clientes recibidos
        listaClientes.setAll(clientes);
    }

    private void limpiarDetalle() {
        etiquetaNombre.setText("Selecciona un cliente");
        etiquetaDni.setText("DNI:");
        etiquetaTelefono.setText("Teléfono:");
        etiquetaEmail.setText("Correo:");
        etiquetaFechaNac.setText("Fecha de nacimiento:");
    }
}