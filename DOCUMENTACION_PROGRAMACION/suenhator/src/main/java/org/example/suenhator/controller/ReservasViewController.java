package org.example.suenhator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.suenhator.dao.ClienteDAO;
import org.example.suenhator.dao.PersonalizacionDAO;
import org.example.suenhator.dao.ReservaDAO;
import org.example.suenhator.model.Cliente;
import org.example.suenhator.model.Personalizacion;
import org.example.suenhator.model.Reserva;
import org.example.suenhator.model.enums.EstadoReserva;
import org.example.suenhator.utils.ViewLoader;

import java.net.URL;
import java.time.LocalDate;
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
    private Button botonCompletarReserva;

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
    private Label etiquetaDescripcionPersonalizacionReserva;

    private ReservaDAO reservaDAO;
    private ClienteDAO clienteDAO;
    private PersonalizacionDAO personalizacionDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        reservaDAO = new ReservaDAO();
        clienteDAO = new ClienteDAO();
        personalizacionDAO = new PersonalizacionDAO();
        listaReservas = FXCollections.observableArrayList();
    }

    private void initGUI() {
        listaReservas.setAll(reservaDAO.consultarReservasPorFecha(LocalDate.now()));
        listViewReservas.setItems(listaReservas);
        limpiarDetalle();
    }

    private void actions() {

        botonAbrirFormularioNuevaReserva.setOnAction(event ->
                cargarVista("formReserva-view.fxml", "Formulario de reserva"));

        botonModificarReservaSeleccionada.setOnAction(event -> {
            Reserva reservaSeleccionada = listViewReservas.getSelectionModel().getSelectedItem();

            if (reservaSeleccionada == null) {
                crearWarning("Sin selección", "Debes seleccionar una reserva para modificarla");
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/org/example/suenhator/formReserva-view.fxml")
                );
                Node vista = loader.load();

                FormularioReservaViewController controller = loader.getController();
                controller.cargarReserva(reservaSeleccionada);

                ViewLoader.cargarVista(vista, "Modificar reserva");

            } catch (Exception e) {
                e.printStackTrace();
                crearWarning("Error", "No se pudo abrir el formulario de reserva");
            }
        });

        botonBuscarReserva.setOnAction(event -> buscarReservas());

        listViewReservas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                limpiarDetalle();
                return;
            }
            mostrarDetalle(newValue);
        });

        botonConfirmarReserva.setOnAction(event -> {
            Reserva reservaSeleccionada = listViewReservas.getSelectionModel().getSelectedItem();

            if (reservaSeleccionada == null) {
                crearWarning("Sin selección", "Debes seleccionar una reserva");
                return;
            }

            boolean cambiado = reservaDAO.cambiarEstadoReserva(
                    reservaSeleccionada.getCliente().getDni(),
                    reservaSeleccionada.getFecha(),
                    reservaSeleccionada.getHora(),
                    EstadoReserva.confirmada
            );

            if (cambiado) {
                reservaSeleccionada.setEstado(EstadoReserva.confirmada);
                reservaSeleccionada.setEsConfirmado(true);
                mostrarDetalle(reservaSeleccionada);
                listViewReservas.refresh();
                crearInformation("Reserva confirmada", "La reserva se ha confirmado correctamente");
            } else {
                crearWarning("Error", "No se pudo confirmar la reserva");
            }
        });

        botonCancelarReserva.setOnAction(event -> {
            Reserva reservaSeleccionada = listViewReservas.getSelectionModel().getSelectedItem();

            if (reservaSeleccionada == null) {
                crearWarning("Sin selección", "Debes seleccionar una reserva");
                return;
            }

            boolean cambiada = reservaDAO.anularReserva(
                    reservaSeleccionada.getCliente().getDni(),
                    reservaSeleccionada.getFecha(),
                    reservaSeleccionada.getHora()
            );

            if (cambiada) {
                reservaSeleccionada.setEstado(EstadoReserva.cancelada);
                reservaSeleccionada.setEsConfirmado(false);
                mostrarDetalle(reservaSeleccionada);
                listViewReservas.refresh();
                crearInformation("Reserva cancelada", "La reserva se ha cancelado correctamente");
            } else {
                crearWarning("Error", "No se pudo cancelar la reserva");
            }
        });

        botonCompletarReserva.setOnAction(event -> {
            Reserva reservaSeleccionada = listViewReservas.getSelectionModel().getSelectedItem();

            if (reservaSeleccionada == null) {
                crearWarning("Sin selección", "Debes seleccionar una reserva");
                return;
            }

            boolean cambiada = reservaDAO.completarReserva(
                    reservaSeleccionada.getCliente().getDni(),
                    reservaSeleccionada.getFecha(),
                    reservaSeleccionada.getHora()
            );

            if (cambiada) {
                reservaSeleccionada.setEstado(EstadoReserva.completada);
                reservaSeleccionada.setEsConfirmado(true);
                mostrarDetalle(reservaSeleccionada);
                listViewReservas.refresh();
                crearInformation("Reserva completada", "La reserva se ha marcado como completada");
            } else {
                crearWarning("Error", "No se pudo completar la reserva");
            }
        });
    }

    private void buscarReservas() {
        String dni = campoTextoDniClienteReserva.getText();
        boolean hayDni = dni != null && !dni.isBlank();
        boolean hayFecha = selectorFiltroFechaReserva.getValue() != null;

        listaReservas.clear();
        limpiarDetalle();

        if (!hayDni && !hayFecha) {
            return;
        }

        if (hayDni && hayFecha) {
            Cliente clienteEncontrado = clienteDAO.buscarPorDni(dni.trim());

            if (clienteEncontrado == null) {
                crearWarning("Cliente no encontrado", "No existe ningún cliente con ese DNI");
                return;
            }

            listaReservas.setAll(reservaDAO.consultarReservasPorClienteYFecha(
                    clienteEncontrado,
                    selectorFiltroFechaReserva.getValue()
            ));

        } else if (hayDni) {
            Cliente clienteEncontrado = clienteDAO.buscarPorDni(dni.trim());

            if (clienteEncontrado == null) {
                crearWarning("Cliente no encontrado", "No existe ningún cliente con ese DNI");
                return;
            }

            listaReservas.setAll(reservaDAO.consultarReservasPorCliente(clienteEncontrado));

        } else {
            listaReservas.setAll(reservaDAO.consultarReservasPorFecha(selectorFiltroFechaReserva.getValue()));
        }

        if (listaReservas.isEmpty()) {
            crearWarning("Sin resultados", "No se han encontrado reservas");
        }
    }

    private void limpiarDetalle() {
        etiquetaTituloReservaSeleccionada.setText("Selecciona una reserva");
        etiquetaClienteReserva.setText("Cliente");
        etiquetaPackReserva.setText("Pack");
        etiquetaSalaReserva.setText("Sala");
        etiquetaSupervisorReserva.setText("Supervisor");
        etiquetaFechaHoraReserva.setText("Fecha y hora");
        etiquetaDuracionReserva.setText("Duración aplicada");
        etiquetaEstadoReserva.setText("Estado");
        etiquetaDescripcionPersonalizacionReserva.setText("No hay personalización asociada.");
        actualizarBotonesSegunEstado(null);
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

        Personalizacion personalizacion = personalizacionDAO.obtenerPersonalizacionPorReserva(reservaSeleccionada);
        String descripcionPersonalizacion = personalizacion != null && personalizacion.getDescripcion() != null && !personalizacion.getDescripcion().isBlank()
                ? personalizacion.getDescripcion()
                : "No hay personalización asociada.";
        etiquetaDescripcionPersonalizacionReserva.setText(descripcionPersonalizacion);
        actualizarBotonesSegunEstado(reservaSeleccionada);
    }

    private void actualizarBotonesSegunEstado(Reserva reserva) {
        boolean esCompletada = reserva != null && reserva.getEstado() == EstadoReserva.completada;
        botonModificarReservaSeleccionada.setDisable(esCompletada);
        botonConfirmarReserva.setDisable(esCompletada);
        botonCancelarReserva.setDisable(esCompletada);
        botonCompletarReserva.setDisable(esCompletada);
    }
}