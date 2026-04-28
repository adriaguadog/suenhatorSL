package org.example.suenhator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.suenhator.dao.ClienteDAO;
import org.example.suenhator.dao.InvitadoDAO;
import org.example.suenhator.dao.PersonalizacionDAO;
import org.example.suenhator.dao.ReservaDAO;
import org.example.suenhator.dao.SalaDAO;
import org.example.suenhator.model.Cliente;
import org.example.suenhator.model.Invitado;
import org.example.suenhator.model.Pack;
import org.example.suenhator.model.Personalizacion;
import org.example.suenhator.model.Reserva;
import org.example.suenhator.model.Sala;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.suenhator.controller.ComprasViewController;
import org.example.suenhator.dao.CompraDAO;
import org.example.suenhator.model.Compra;
import org.example.suenhator.model.LineaCompra;
import org.example.suenhator.model.Supervisor;
import org.example.suenhator.model.enums.EstadoPersonalizacion;
import org.example.suenhator.model.enums.EstadoReserva;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearInformation;
import static org.example.suenhator.utils.AlertCreation.crearWarning;
import static org.example.suenhator.utils.ViewLoader.cargarVista;

public class FormularioReservaViewController implements Initializable {

    @FXML
    private TextField campoTextoDni;

    @FXML
    private TextField campoHora;

    @FXML
    private TextField campoVideo;

    @FXML
    private TextArea areaDescripcionPersonalizacion;

    @FXML
    private Label etiquetaClienteEncontrado;

    @FXML
    private ComboBox<EstadoPersonalizacion> selectorEstadoPersonalizacion;

    @FXML
    private ComboBox<EstadoReserva> selectorEstadoReserva;

    @FXML
    private DatePicker selectorFecha;

    @FXML
    private ComboBox<Pack> selectorPack;

    @FXML
    private ComboBox<Sala> selectorSala;

    @FXML
    private ComboBox<Supervisor> selectorSupervisor;

    @FXML
    private Button botonBuscarCliente;

    @FXML
    private Button botonGuardar;

    @FXML
    private Button botonLimpiar;

    @FXML
    private Button botonCancelar;

    @FXML
    private TextField campoNombreInvitado;

    @FXML
    private TextField campoApellidosInvitado;

    @FXML
    private TextField campoDniInvitado;

    @FXML
    private TextField campoTelefonoInvitado;

    @FXML
    private TextField campoEmailInvitado;

    @FXML
    private DatePicker selectorFechaNacInvitado;

    @FXML
    private Button botonGuardarInvitado;

    @FXML
    private Button botonLimpiarInvitado;

    @FXML
    private Button botonQuitarInvitado;

    @FXML
    private ListView<Invitado> listViewInvitados;

    @FXML
    private Label etiquetaTotalInvitados;

    private ObservableList<Pack> listaPacks;
    private ObservableList<Sala> listaSalas;
    private ObservableList<Supervisor> listaSupervisores;
    private ObservableList<EstadoReserva> listaEstadosReserva;
    private ObservableList<EstadoPersonalizacion> listaEstadosPersonalizacion;
    private ObservableList<Invitado> listaInvitados;

    private ClienteDAO clienteDAO;
    private ReservaDAO reservaDAO;
    private PersonalizacionDAO personalizacionDAO;
    private SalaDAO salaDAO;
    private InvitadoDAO invitadoDAO;

    private Reserva reservaSeleccionada;
    private Cliente clienteEncontrado;
    private Invitado invitadoSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        clienteDAO = new ClienteDAO();
        reservaDAO = new ReservaDAO();
        personalizacionDAO = new PersonalizacionDAO();
        salaDAO = new SalaDAO();
        invitadoDAO = new InvitadoDAO();

        listaPacks = FXCollections.observableArrayList(reservaDAO.listarPacks());
        listaSalas = FXCollections.observableArrayList(salaDAO.obtenerSalas());
        listaSupervisores = FXCollections.observableArrayList(reservaDAO.listarSupervisores());
        listaEstadosReserva = FXCollections.observableArrayList(EstadoReserva.values());
        listaEstadosPersonalizacion = FXCollections.observableArrayList(EstadoPersonalizacion.values());
        listaInvitados = FXCollections.observableArrayList();
    }

    private void initGUI() {
        selectorPack.setItems(listaPacks);
        selectorSala.setItems(listaSalas);
        selectorSupervisor.setItems(listaSupervisores);
        selectorEstadoReserva.setItems(listaEstadosReserva);
        selectorEstadoPersonalizacion.setItems(listaEstadosPersonalizacion);
        listViewInvitados.setItems(listaInvitados);

        limpiarFormulario();
    }

    private void actions() {
        botonLimpiar.setOnAction(event -> limpiarFormulario());
        botonCancelar.setOnAction(event -> cargarVista("reservas-view.fxml", "Reservas"));

        botonBuscarCliente.setOnAction(event -> {
            if (campoTextoDni.getText() == null || campoTextoDni.getText().isBlank()) {
                crearWarning("DNI vacío", "Debes introducir un DNI");
                return;
            }

            clienteEncontrado = clienteDAO.buscarPorDni(campoTextoDni.getText().trim());

            if (clienteEncontrado == null) {
                etiquetaClienteEncontrado.setText("Cliente no encontrado");
                crearWarning("Cliente no encontrado", "No existe ningún cliente con ese DNI");
            } else {
                etiquetaClienteEncontrado.setText(clienteEncontrado.getNombre() + " " + clienteEncontrado.getApellidos());
            }
        });

        botonGuardar.setOnAction(event -> guardarReserva());

        botonGuardarInvitado.setOnAction(event -> guardarInvitado());
        botonLimpiarInvitado.setOnAction(event -> limpiarFormularioInvitado());

        botonQuitarInvitado.setOnAction(event -> {
            Invitado invitado = listViewInvitados.getSelectionModel().getSelectedItem();

            if (invitado == null) {
                crearWarning("Sin selección", "Debes seleccionar un invitado");
                return;
            }

            listaInvitados.remove(invitado);
            actualizarContadorInvitados();

            if (invitadoSeleccionado == invitado) {
                limpiarFormularioInvitado();
            }

            crearInformation("Invitado eliminado", "El invitado se ha eliminado de la lista");
        });

        listViewInvitados.setOnMouseClicked(event -> {
            Invitado invitado = listViewInvitados.getSelectionModel().getSelectedItem();

            if (invitado != null) {
                invitadoSeleccionado = invitado;
                cargarInvitado(invitado);
            }
        });
    }

    private void guardarReserva() {
        if (clienteEncontrado == null) {
            crearWarning("Cliente no seleccionado", "Debes buscar y seleccionar un cliente");
            return;
        }

        if (selectorPack.getSelectionModel().getSelectedItem() == null
                || selectorSala.getSelectionModel().getSelectedItem() == null
                || selectorSupervisor.getSelectionModel().getSelectedItem() == null
                || selectorFecha.getValue() == null
                || campoHora.getText() == null
                || campoHora.getText().isBlank()) {
            crearWarning("Datos incompletos", "Debes rellenar los datos obligatorios de la reserva");
            return;
        }

        LocalTime horaReserva;

        try {
            horaReserva = LocalTime.parse(campoHora.getText().trim());
        } catch (Exception e) {
            crearWarning("Hora incorrecta", "Introduce la hora con formato HH:mm");
            return;
        }

        EstadoReserva estadoSeleccionado = selectorEstadoReserva.getSelectionModel().getSelectedItem();

        if (estadoSeleccionado == null) {
            crearWarning("Estado no seleccionado", "Debes seleccionar el estado de la reserva");
            return;
        }

        if (reservaSeleccionada == null) {
            Reserva reservaNueva = reservaDAO.crearReserva(
                    clienteEncontrado,
                    selectorSala.getSelectionModel().getSelectedItem(),
                    selectorPack.getSelectionModel().getSelectedItem(),
                    selectorSupervisor.getSelectionModel().getSelectedItem(),
                    selectorFecha.getValue(),
                    horaReserva,
                    estadoSeleccionado
            );

            if (reservaNueva == null) {
                crearWarning("Error", "No se pudo guardar la reserva");
                return;
            }

            reservaNueva.setCliente(clienteEncontrado);
            reservaNueva.setSala(selectorSala.getSelectionModel().getSelectedItem());
            reservaNueva.setPack(selectorPack.getSelectionModel().getSelectedItem());
            reservaNueva.setSupervisor(selectorSupervisor.getSelectionModel().getSelectedItem());
            reservaNueva.setFecha(selectorFecha.getValue());
            reservaNueva.setHora(horaReserva);
            reservaNueva.setEstado(estadoSeleccionado);

            guardarPersonalizacionAsociada(reservaNueva);

            crearInformation("Reserva guardada", "La reserva se ha creado correctamente");
            procesarDespuesGuardadoReserva(reservaNueva);
        } else {
            boolean modificada = reservaDAO.modificarReserva(
                    reservaSeleccionada.getIdReserva(),
                    selectorSala.getSelectionModel().getSelectedItem(),
                    selectorPack.getSelectionModel().getSelectedItem(),
                    selectorSupervisor.getSelectionModel().getSelectedItem(),
                    selectorFecha.getValue(),
                    horaReserva,
                    estadoSeleccionado
            );

            if (!modificada) {
                crearWarning("Error", "No se pudo modificar la reserva");
                return;
            }

            guardarPersonalizacionAsociada(reservaSeleccionada);
            crearInformation("Reserva modificada", "La reserva se ha modificado correctamente");
            procesarDespuesGuardadoReserva(reservaSeleccionada);
        }
    }

    private void procesarDespuesGuardadoReserva(Reserva reserva) {
        if (reserva == null) {
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Pago ahora");
        alerta.setHeaderText("¿Deseas pagar ahora?");
        alerta.setContentText("Si eliges sí, irás a la pantalla de compras con la reserva cargada. Si eliges no, se generará la compra y volverás a reservas.");

        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonSi) {
            abrirVistaComprasConReserva(reserva);
        } else {
            generarCompraDesdeReserva(reserva);
            cargarVista("reservas-view.fxml", "Reservas");
        }
    }

    private void abrirVistaComprasConReserva(Reserva reserva) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/suenhator/compras-view.fxml"));
            Node vista = loader.load();
            ComprasViewController comprasController = loader.getController();
            comprasController.cargarCompraDesdeReserva(reserva);
            cargarVista(vista, "Compras");
        } catch (IOException e) {
            crearWarning("Error al abrir compras", e.getMessage());
            cargarVista("reservas-view.fxml", "Reservas");
        }
    }

    private void generarCompraDesdeReserva(Reserva reserva) {
        if (reserva == null || reserva.getCliente() == null || reserva.getPack() == null) {
            return;
        }

        CompraDAO compraDAO = new CompraDAO();
        Compra compra = compraDAO.registrarCompra(reserva.getCliente());

        if (compra == null) {
            crearWarning("Error compra", "No se pudo generar la compra pendiente");
            return;
        }

        compra.setIdReserva(reserva.getIdReserva());

        double precioUnitario = reserva.getPack().getPrecio();
        LineaCompra linea = new LineaCompra(compra, reserva.getPack(), 1, precioUnitario, precioUnitario);

        if (!compraDAO.registrarLineaCompra(compra, linea)) {
            crearWarning("Error compra", "No se pudo guardar la línea de compra");
            return;
        }

        compraDAO.actualizarTotalCompra(compra, precioUnitario);
    }

    private void guardarInvitado() {
        if (campoNombreInvitado.getText() == null || campoNombreInvitado.getText().isBlank()
                || campoApellidosInvitado.getText() == null || campoApellidosInvitado.getText().isBlank()
                || campoDniInvitado.getText() == null || campoDniInvitado.getText().isBlank()) {
            crearWarning("Datos incompletos", "Nombre, apellidos y DNI son obligatorios");
            return;
        }

        boolean dniRepetido = listaInvitados.stream()
                .anyMatch(invitado ->
                        invitado.getDni() != null
                                && invitado.getDni().equalsIgnoreCase(campoDniInvitado.getText())
                                && invitado != invitadoSeleccionado);

        if (dniRepetido) {
            crearWarning("DNI repetido", "Ya hay un invitado con ese DNI en la lista");
            return;
        }

        if (invitadoSeleccionado == null) {
            Invitado invitadoNuevo = new Invitado(
                    campoNombreInvitado.getText(),
                    campoApellidosInvitado.getText(),
                    campoDniInvitado.getText(),
                    campoTelefonoInvitado.getText(),
                    campoEmailInvitado.getText(),
                    selectorFechaNacInvitado.getValue()
            );
            listaInvitados.add(invitadoNuevo);
            crearInformation("Invitado guardado", "El invitado se ha añadido correctamente");
        } else {
            invitadoSeleccionado.setNombre(campoNombreInvitado.getText());
            invitadoSeleccionado.setApellidos(campoApellidosInvitado.getText());
            invitadoSeleccionado.setDni(campoDniInvitado.getText());
            invitadoSeleccionado.setTelefono(campoTelefonoInvitado.getText());
            invitadoSeleccionado.setEmail(campoEmailInvitado.getText());
            invitadoSeleccionado.setFechaNac(selectorFechaNacInvitado.getValue());
            listViewInvitados.refresh();
            crearInformation("Invitado modificado", "El invitado se ha modificado correctamente");
        }

        actualizarContadorInvitados();
        limpiarFormularioInvitado();
    }

    private void limpiarFormulario() {
        reservaSeleccionada = null;
        clienteEncontrado = null;

        campoTextoDni.clear();
        campoHora.clear();
        campoVideo.clear();
        areaDescripcionPersonalizacion.clear();

        etiquetaClienteEncontrado.setText("Cliente no seleccionado");

        selectorPack.getSelectionModel().clearSelection();
        selectorSala.getSelectionModel().clearSelection();
        selectorSupervisor.getSelectionModel().clearSelection();
        selectorEstadoReserva.getSelectionModel().clearSelection();
        selectorEstadoPersonalizacion.getSelectionModel().clearSelection();
        selectorFecha.setValue(null);

        listaInvitados.clear();
        limpiarFormularioInvitado();
        actualizarContadorInvitados();
    }

    private void limpiarFormularioInvitado() {
        invitadoSeleccionado = null;
        campoNombreInvitado.clear();
        campoApellidosInvitado.clear();
        campoDniInvitado.clear();
        campoTelefonoInvitado.clear();
        campoEmailInvitado.clear();
        selectorFechaNacInvitado.setValue(null);
        listViewInvitados.getSelectionModel().clearSelection();
    }

    private void cargarInvitado(Invitado invitado) {
        if (invitado == null) {
            return;
        }

        campoNombreInvitado.setText(invitado.getNombre());
        campoApellidosInvitado.setText(invitado.getApellidos());
        campoDniInvitado.setText(invitado.getDni());
        campoTelefonoInvitado.setText(invitado.getTelefono());
        campoEmailInvitado.setText(invitado.getEmail());
        selectorFechaNacInvitado.setValue(invitado.getFechaNac());
    }

    private void actualizarContadorInvitados() {
        etiquetaTotalInvitados.setText(String.valueOf(listaInvitados.size()));
    }

    public void cargarReserva(Reserva reserva) {
        limpiarFormulario();

        reservaSeleccionada = reserva;

        if (reservaSeleccionada == null) {
            return;
        }

        if (reservaSeleccionada.getCliente() != null) {
            clienteEncontrado = clienteDAO.buscarPorDni(reservaSeleccionada.getCliente().getDni());

            if (clienteEncontrado == null) {
                clienteEncontrado = reservaSeleccionada.getCliente();
            }

            campoTextoDni.setText(clienteEncontrado.getDni());
            etiquetaClienteEncontrado.setText(clienteEncontrado.getNombre() + " " + clienteEncontrado.getApellidos());
        }

        selectorFecha.setValue(reservaSeleccionada.getFecha());

        if (reservaSeleccionada.getHora() != null) {
            campoHora.setText(reservaSeleccionada.getHora().toString());
        }

        seleccionarPackPorId(reservaSeleccionada.getPack());
        seleccionarSalaPorId(reservaSeleccionada.getSala());
        seleccionarSupervisorPorId(reservaSeleccionada.getSupervisor());

        if (reservaSeleccionada.getEstado() != null) {
            selectorEstadoReserva.setValue(reservaSeleccionada.getEstado());
        }

        if (reservaSeleccionada.getIdReserva() > 0) {
            Personalizacion personalizacionExistente = personalizacionDAO.obtenerPersonalizacionPorReserva(reservaSeleccionada);

            if (personalizacionExistente != null) {
                campoVideo.setText(personalizacionExistente.getVideoRef());
                areaDescripcionPersonalizacion.setText(personalizacionExistente.getDescripcion());
                selectorEstadoPersonalizacion.setValue(personalizacionExistente.getEstado());
            }

            listaInvitados.setAll(invitadoDAO.obtenerInvitadosPorReserva(reservaSeleccionada));
        }

        actualizarContadorInvitados();
    }

    private void guardarPersonalizacionAsociada(Reserva reserva) {
        if (reserva == null || reserva.getIdReserva() <= 0) {
            return;
        }

        String videoRef = campoVideo.getText();
        String descripcion = areaDescripcionPersonalizacion.getText();
        EstadoPersonalizacion estado = selectorEstadoPersonalizacion.getSelectionModel().getSelectedItem();

        Personalizacion personalizacion = personalizacionDAO.obtenerPersonalizacionPorReserva(reserva);

        if (personalizacion == null) {
            if ((videoRef == null || videoRef.isBlank())
                    && (descripcion == null || descripcion.isBlank())
                    && estado == null) {
                return;
            }
            personalizacion = new Personalizacion();
            personalizacion.setReserva(reserva);
        }

        personalizacion.setVideoRef(videoRef);
        personalizacion.setDescripcion(descripcion);
        personalizacion.setEstado(estado);

        if (personalizacion.getIdPersonalizacion() > 0) {
            personalizacionDAO.actualizarPersonalizacion(personalizacion);
        } else {
            personalizacionDAO.registrarPersonalizacion(personalizacion);
        }
    }

    private void seleccionarPackPorId(Pack packReserva) {
        if (packReserva == null) {
            return;
        }

        for (Pack pack : listaPacks) {
            if (pack.getIdPack() == packReserva.getIdPack()) {
                selectorPack.setValue(pack);
                return;
            }
        }

        selectorPack.setValue(packReserva);
    }

    private void seleccionarSalaPorId(Sala salaReserva) {
        if (salaReserva == null) {
            return;
        }

        for (Sala sala : listaSalas) {
            if (sala.getIdSala() == salaReserva.getIdSala()) {
                selectorSala.setValue(sala);
                return;
            }
        }

        selectorSala.setValue(salaReserva);
    }

    private void seleccionarSupervisorPorId(Supervisor supervisorReserva) {
        if (supervisorReserva == null) {
            return;
        }

        for (Supervisor supervisor : listaSupervisores) {
            if (supervisor.getIdSupervisor() == supervisorReserva.getIdSupervisor()) {
                selectorSupervisor.setValue(supervisor);
                return;
            }
        }

        selectorSupervisor.setValue(supervisorReserva);
    }
}