package org.example.suenhator.controller;

import controller.ClienteController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.example.suenhator.data.Dataset;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearInformation;
import static org.example.suenhator.utils.AlertCreation.crearWarning;
import static org.example.suenhator.utils.ViewLoader.cargarVista;

public class ClientesViewController implements Initializable {

    private ClienteController clienteController;
    private ObservableList<Cliente> listaClientesMostrada;

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
        listaClientesMostrada = FXCollections.observableArrayList();
    }

    private void initGUI() {
        //asocio la listview a una lista observable para no modificar directamente la del dataset
        listaClientesFiltrados.setItems(listaClientesMostrada);
        //cargo todos los clientes del dataset al iniciar
        cargarClientes(Dataset.listaClientes);
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

                //quiero cargar el loader con el cliente para pasarlo al registro
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
            if (clienteController.darDeBaja(clienteSeleccionado)) {
                crearInformation("Accion completada", "Cliente eliminado correctamente");
                //recargo la lista mostrada con la lista actual del dataset
                cargarClientes(Dataset.listaClientes);
            }
        });

        botonBuscarClientePorDni.setOnAction(event -> {
            //cojo el texto
            String dni = campoTextoBusquedaClientes.getText();

            //dni vacio?
            if (dni == null || dni.isBlank()) {
                cargarClientes(Dataset.listaClientes);
                crearWarning("Error", "El dni no puede estar vacio");
                return;
            }

            Cliente clienteEncontrado = clienteController.buscarPorDni(dni);

            if (clienteEncontrado != null) {
                //muestro solo el cliente encontrado en la lista visible
                listaClientesMostrada.setAll(clienteEncontrado);
                listaClientesFiltrados.getSelectionModel().select(clienteEncontrado);
            } else {
                //si no se encuentra, dejo la lista vacia y aviso
                listaClientesMostrada.clear();
                crearWarning("Cliente no encontrado", "No se ha encontrado ningun usuario asociado al dni");
            }
        });
    }

    //metodos
    private void cargarClientes(ObservableList<Cliente> clientes) {
        //cargo en la lista visible los clientes recibidos
        listaClientesMostrada.setAll(clientes);
    }
}