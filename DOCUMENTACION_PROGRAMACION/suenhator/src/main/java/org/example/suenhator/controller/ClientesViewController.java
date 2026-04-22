package org.example.suenhator.controller;

import controller.ClienteController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Cliente;
import org.example.suenhator.HelloApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private ListView<Cliente> listaClientesFiltrados;


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
        cargarClientes(clienteController.listarTodos());
    }

    private void actions() {

        botonNuevoCliente.setOnAction(event -> {
            cargarVista("registro-view.fxml", botonNuevoCliente, "Panel principal");

        });

        botonModificarCliente.setOnAction(event -> {
            //cliente que voy a transferir a otra vista
            Cliente clienteSeleccionado = listaClientesFiltrados.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado == null) {
                crearWarning("Sin selección", "Debes seleccionar un cliente para modificarlo");
                return;
            }
            try {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("registro-view.fxml"));
                Scene scene = new Scene(loader.load(), 880, 640);

                //quiaro cargar el loader con el cliente para pasarlo al registro
                RegistroViewController controller = loader.getController();
                controller.cargarCliente(clienteSeleccionado);
//abro el form y cierro esta ventana
                Stage stage = new Stage();
                stage.setTitle("Modificar cliente");
                stage.setScene(scene);
                stage.show();
                ((Stage) botonModificarCliente.getScene().getWindow()).close();
            } catch (IOException e) {
                crearWarning("Error", "No se ha podido abrir el formulario de modificación");
            }
        });

        botonDarDeBajaCliente.setOnAction(event -> {
            Cliente clienteSeleccionado = listaClientesFiltrados.getSelectionModel().getSelectedItem();
            if (clienteController.darDeBaja(clienteSeleccionado)){
                crearInformation("Accion completada", "Cliente eliminado correctamente");
            }
        });

        botonBuscarClientePorDni.setOnAction(event -> {
            //cojo el texto
            String dni = campoTextoBusquedaClientes.getText();
            //dni vacio?
            if (dni.isEmpty()) {
                cargarClientes(clienteController.listarTodos());
                crearWarning("Error", "El dni no puede estar vacio");
                return;
            }

            Cliente clienteEncontrado = clienteController.buscarPorDni(dni);
            //vacio la lista
            listaClientesFiltrados.getItems().clear();

            if (clienteEncontrado != null) {
                listaClientesFiltrados.getItems().add(clienteEncontrado);
                listaClientesFiltrados.getSelectionModel().select(clienteEncontrado);
            } else {
                crearWarning("Cliente no encontrado", "No se ha encontrado ningun usuario asociado al dni");
            }
        });
    }


    //metodos

    private void cargarClientes(ArrayList<Cliente> clientes) {
        //limpio lista
        listaClientesFiltrados.getItems().clear();
        //anhado clientes
        listaClientesFiltrados.getItems().addAll(clientes);
    }
}