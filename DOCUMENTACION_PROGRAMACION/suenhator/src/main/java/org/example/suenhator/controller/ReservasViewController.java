package org.example.suenhator.controller;

import controller.ClienteController;
import controller.ReservaController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Cliente;
import model.Reserva;
import model.enums.EstadoReserva;
import org.example.suenhator.data.Dataset;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearInformation;
import static org.example.suenhator.utils.AlertCreation.crearWarning;
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
    private Button botonCancelarReserva;

    @FXML
    private TextField campoTextoDniClienteReserva;

    @FXML
    private DatePicker selectorFiltroFechaReserva;

    @FXML
    private ListView<Reserva> listViewReservas;
    private ObservableList<Reserva> listaReservas;

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
    private Label etiquetaPersonalizacionReserva;

    private ReservaController reservaController;
    private ClienteController clienteController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        //inicializo controladores y lista
        reservaController = new ReservaController();
        clienteController = new ClienteController();
        listaReservas = FXCollections.observableArrayList();
    }

    private void initGUI() {
        //cargo todas las reservas al empezar
        listaReservas.setAll(Dataset.listaReservas);
        listViewReservas.setItems(listaReservas);

        //dejo el detalle limpio
        limpiarDetalle();
    }

    private void actions() {
        botonAbrirFormularioNuevaReserva.setOnAction(event ->
                cargarVista("formReserva-view.fxml", "Formulario de reserva"));

        botonModificarReservaSeleccionada.setOnAction(event -> {
            Reserva reservaSeleccionada = listViewReservas.getSelectionModel().getSelectedItem();

            //compruebo si hay reserva seleccionada
            if (reservaSeleccionada == null) {
                crearWarning("Sin selección", "Debes seleccionar una reserva para modificarla");
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/suenhator/formReserva-view.fxml"));
                Scene scene = new Scene(loader.load());

                FormularioReservaViewController controller = loader.getController();
                controller.cargarReserva(reservaSeleccionada);

                Stage stage = new Stage();
                stage.setTitle("Modificar reserva");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                crearWarning("Error", "No se pudo abrir el formulario de reserva");
            }
        });

        botonBuscarReserva.setOnAction(event -> {
            //si no ha puesto nada
            if ((campoTextoDniClienteReserva.getText() == null || campoTextoDniClienteReserva.getText().isBlank())
                    && selectorFiltroFechaReserva.getValue() == null) {
                listaReservas.setAll(Dataset.listaReservas);
                limpiarDetalle();
                return;
            }

            //si ha puesto dni
            if (campoTextoDniClienteReserva.getText() != null && !campoTextoDniClienteReserva.getText().isBlank()) {
                Cliente clienteEncontrado = clienteController.buscarPorDni(campoTextoDniClienteReserva.getText());

                //compruebo si existe
                if (clienteEncontrado == null) {
                    crearWarning("Cliente no encontrado", "No existe ningún cliente con ese DNI");
                    return;
                }
                //si llego  aqui es que lo he encontrado
                List<Reserva> reservasCliente = reservaController.consultarReservasPorCliente(clienteEncontrado);
                listaReservas.setAll(reservasCliente);
            }

            //si ha puesto fecha y no dni, busco por fecha
            if ((campoTextoDniClienteReserva.getText() == null || campoTextoDniClienteReserva.getText().isBlank())
                    && selectorFiltroFechaReserva.getValue() != null) {
                List<Reserva> reservasFecha = reservaController.consultarReservasPorFecha(selectorFiltroFechaReserva.getValue());
                listaReservas.setAll(reservasFecha);
            }
            limpiarDetalle();

            //si no hay resultados aviso
            if (listaReservas.isEmpty()) {
                crearWarning("Sin resultados", "No se han encontrado reservas");
            }
        });

        //cuando pincho en una
        listViewReservas.setOnMouseClicked(event -> {
            Reserva reservaSeleccionada = listViewReservas.getSelectionModel().getSelectedItem();

            //compruebo si hay selección
            if (reservaSeleccionada == null) {
                return;
            }
            //quiero que se muestre el detalle cuando pincho en una reserva
            mostrarDetalle(reservaSeleccionada);
        });

        botonConfirmarReserva.setOnAction(event -> {
            Reserva reservaSeleccionada = listViewReservas.getSelectionModel().getSelectedItem();

            //compruebo si hay selección
            if (reservaSeleccionada == null) {
                crearWarning("Sin selección", "Debes seleccionar una reserva");
                return;
            }

            boolean cambiado = reservaController.cambiarEstadoReserva(reservaSeleccionada.getCliente().getDni(),
                    reservaSeleccionada.getFecha(), reservaSeleccionada.getHora(),
                    EstadoReserva.CONFIRMADA
            );

            if (cambiado) {
                reservaSeleccionada.setEstado(EstadoReserva.CONFIRMADA);
                mostrarDetalle(reservaSeleccionada);
                listViewReservas.refresh();
                crearInformation("Reserva confirmada", "La reserva se ha confirmado correctamente");
            } else {
                crearWarning("Error", "No se pudo confirmar la reserva");
            }
        });

        botonCancelarReserva.setOnAction(event -> {
            Reserva reservaSeleccionada = listViewReservas.getSelectionModel().getSelectedItem();

            //compruebo si hay selección
            if (reservaSeleccionada == null) {
                crearWarning("Sin selección", "Debes seleccionar una reserva");
                return;
            }

            boolean cambiada = reservaController.anularReserva(
                    reservaSeleccionada.getCliente().getDni(),
                    reservaSeleccionada.getFecha(),
                    reservaSeleccionada.getHora()
            );

            if (cambiada) {
                reservaSeleccionada.setEstado(EstadoReserva.CANCELADA);
                mostrarDetalle(reservaSeleccionada);
                listViewReservas.refresh();
                crearInformation("Reserva cancelada", "La reserva se ha cancelado correctamente");
            } else {
                crearWarning("Error", "No se pudo cancelar la reserva");
            }
        });
    }

    private void limpiarDetalle() {
        etiquetaTituloReservaSeleccionada.setText("Selecciona una reserva");
        etiquetaClienteReserva.setText("Cliente:");
        etiquetaPackReserva.setText("Pack:");
        etiquetaSalaReserva.setText("Sala:");
        etiquetaSupervisorReserva.setText("Supervisor:");
        etiquetaFechaHoraReserva.setText("Fecha y hora:");
        etiquetaDuracionReserva.setText("Duración aplicada:");
        etiquetaEstadoReserva.setText("Estado:");
        etiquetaEstadoPersonalizacionReserva.setText("Estado de personalización:");
        etiquetaDescripcionPersonalizacionReserva.setText("La personalización se modifica desde el formulario");
        etiquetaPersonalizacionReserva.setText("");
    }

    private void mostrarDetalle(Reserva reservaSeleccionada) {
        etiquetaTituloReservaSeleccionada.setText("Reserva seleccionada");
        etiquetaClienteReserva.setText("Cliente: " + reservaSeleccionada.getCliente().getNombre() + " " + reservaSeleccionada.getCliente().getApellidos());
        etiquetaPackReserva.setText("Pack: " + reservaSeleccionada.getPack().getNombre());
        etiquetaSalaReserva.setText("Sala: " + reservaSeleccionada.getSala().getNombre());
        etiquetaSupervisorReserva.setText("Supervisor: " + reservaSeleccionada.getSupervisor().getNombre());
        etiquetaFechaHoraReserva.setText("Fecha y hora: " + reservaSeleccionada.getFecha() + " " + reservaSeleccionada.getHora());
        etiquetaDuracionReserva.setText("Duración aplicada: " + reservaSeleccionada.getPack().getDuracion() + " minutos");
        etiquetaEstadoReserva.setText("Estado: " + reservaSeleccionada.getEstado());

        //la personalización aquí solo se muestra la voy a modificar desde el form
        etiquetaEstadoPersonalizacionReserva.setText("Estado de personalización: revisar en el formulario");
        etiquetaPersonalizacionReserva.setText("");
    }
}