package org.example.suenhator.controller;

import controller.ClienteController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Cliente;
import model.Invitado;
import model.Pack;
import model.Personalizacion;
import model.Reserva;
import model.Sala;
import model.Supervisor;
import model.enums.EstadoPersonalizacion;
import model.enums.EstadoReserva;
import org.example.suenhator.data.Dataset;
import org.example.suenhator.utils.ViewLoader;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearInformation;
import static org.example.suenhator.utils.AlertCreation.crearWarning;

public class FormularioReservaViewController implements Initializable {

    @FXML
    private TextArea areaDescripcionPersonalizacion;

    @FXML
    private Button botonFormInvitados;

    @FXML
    private Button botonAnadirInvitado;

    @FXML
    private Button botonBuscarCliente;

    @FXML
    private Button botonGuardar;

    @FXML
    private Button botonLimpiar;

    @FXML
    private Button botonQuitarInvitado;

    @FXML
    private TextField campoHora;

    @FXML
    private TextField campoBusquedaInvitado;

    @FXML
    private TextField campoTextoDni;

    @FXML
    private TextField campoVideo;

    @FXML
    private CheckBox checkConfirmada;

    @FXML
    private Label etiquetaClienteEncontrado;

    @FXML
    private ListView<Invitado> listViewInvitados;
    private ObservableList<Invitado> listaInvitados;

    @FXML
    private ComboBox<EstadoPersonalizacion> selectorEstadoPersonalizacion;
    private ObservableList<EstadoPersonalizacion> listaEstadosPersonalizacion;

    @FXML
    private ComboBox<EstadoReserva> selectorEstadoReserva;
    private ObservableList<EstadoReserva> listaEstadosReserva;

    @FXML
    private DatePicker selectorFecha;

    @FXML
    private ComboBox<Pack> selectorPack;
    private ObservableList<Pack> listaPacks;

    @FXML
    private ComboBox<Sala> selectorSala;
    private ObservableList<Sala> listaSalas;

    @FXML
    private ComboBox<Supervisor> selectorSupervisor;
    private ObservableList<Supervisor> listaSupervisores;

    @FXML
    private Spinner<Integer> spinnerNumeroInvitados;

    private Reserva reservaSeleccionada;
    private ViewLoader viewLoader;
    private ClienteController clienteController;
    private Cliente clienteEncontrado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        // inicializo utilidades y controladores
        viewLoader = new ViewLoader();
        clienteController = new ClienteController();

        // inicializo las listas asociadas a los controles
        listaPacks = FXCollections.observableArrayList(Dataset.listaPacks);
        listaSalas = FXCollections.observableArrayList(Dataset.listaSalas);
        listaSupervisores = FXCollections.observableArrayList(Dataset.listaSupervisores);
        listaEstadosReserva = FXCollections.observableArrayList(EstadoReserva.values());
        listaEstadosPersonalizacion = FXCollections.observableArrayList(EstadoPersonalizacion.values());
        listaInvitados = FXCollections.observableArrayList();
    }

    private void initGUI() {
        // cargo las listas en sus controles
        selectorPack.setItems(listaPacks);
        selectorSala.setItems(listaSalas);
        selectorSupervisor.setItems(listaSupervisores);
        selectorEstadoReserva.setItems(listaEstadosReserva);
        selectorEstadoPersonalizacion.setItems(listaEstadosPersonalizacion);
        listViewInvitados.setItems(listaInvitados);

        // configuro el spinner de invitados
        spinnerNumeroInvitados.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 4, 0, 1));

        limpiarFormulario();
    }

    private void actions() {
        botonLimpiar.setOnAction(event -> {
            limpiarFormulario();
        });

        botonGuardar.setOnAction(event -> {
            // compruebo que el cliente se haya buscado correctamente
            if (clienteEncontrado == null) {
                crearWarning("Cliente no seleccionado", "Debes buscar y seleccionar un cliente");
                return;
            }

            // compruebo campos obligatorios
            if (selectorPack.getSelectionModel().getSelectedItem() == null
                    || selectorSala.getSelectionModel().getSelectedItem() == null
                    || selectorSupervisor.getSelectionModel().getSelectedItem() == null
                    || selectorFecha.getValue() == null
                    || campoHora.getText() == null
                    || campoHora.getText().isBlank()) {

                crearWarning("Datos incompletos", "Debes rellenar los datos obligatorios de la reserva");
                return;
            }

            // convierto la hora
            LocalTime horaReserva;
            try {
                horaReserva = LocalTime.parse(campoHora.getText());
            } catch (Exception e) {
                crearWarning("Hora incorrecta", "Introduce la hora con formato HH:mm");
                return;
            }

            // si no hay reserva seleccionada la creo nueva
            if (reservaSeleccionada == null) {
                Reserva reservaNueva = new Reserva(
                        clienteEncontrado,
                        selectorSala.getSelectionModel().getSelectedItem(),
                        selectorPack.getSelectionModel().getSelectedItem(),
                        selectorSupervisor.getSelectionModel().getSelectedItem(),
                        selectorFecha.getValue(),
                        horaReserva
                );

                if (selectorEstadoReserva.getSelectionModel().getSelectedItem() != null) {
                    reservaNueva.setEstado(selectorEstadoReserva.getSelectionModel().getSelectedItem());
                }

                reservaNueva.setEsConfirmado(checkConfirmada.isSelected());

                Dataset.listaReservas.add(reservaNueva);

                // creo la personalización solo si se ha rellenado algo
                if ((campoVideo.getText() != null && !campoVideo.getText().isBlank())
                        || (areaDescripcionPersonalizacion.getText() != null && !areaDescripcionPersonalizacion.getText().isBlank())
                        || selectorEstadoPersonalizacion.getSelectionModel().getSelectedItem() != null) {

                    Personalizacion personalizacionNueva = new Personalizacion(
                            reservaNueva,
                            campoVideo.getText(),
                            areaDescripcionPersonalizacion.getText(),
                            LocalDate.now(),
                            null,
                            selectorEstadoPersonalizacion.getSelectionModel().getSelectedItem()
                    );

                    // aquí la dejo creada, pero solo podrías guardarla de verdad
                    // si añades listaPersonalizaciones al Dataset
                }

                crearInformation("Reserva guardada", "La reserva se ha creado correctamente");
                limpiarFormulario();

            } else {
                // si ya existe la modifico
                reservaSeleccionada.setCliente(clienteEncontrado);
                reservaSeleccionada.setPack(selectorPack.getSelectionModel().getSelectedItem());
                reservaSeleccionada.setSala(selectorSala.getSelectionModel().getSelectedItem());
                reservaSeleccionada.setSupervisor(selectorSupervisor.getSelectionModel().getSelectedItem());
                reservaSeleccionada.setFecha(selectorFecha.getValue());
                reservaSeleccionada.setHora(horaReserva);
                reservaSeleccionada.setEsConfirmado(checkConfirmada.isSelected());

                if (selectorEstadoReserva.getSelectionModel().getSelectedItem() != null) {
                    reservaSeleccionada.setEstado(selectorEstadoReserva.getSelectionModel().getSelectedItem());
                }

                crearInformation("Reserva modificada", "La reserva se ha modificado correctamente");
            }
        });

        botonBuscarCliente.setOnAction(event -> {
            // compruebo si el dni está vacío
            if (campoTextoDni.getText() == null || campoTextoDni.getText().isBlank()) {
                crearWarning("DNI vacío", "Debes introducir un DNI");
                return;
            }

            // busco el cliente por dni
            clienteEncontrado = clienteController.buscarPorDni(campoTextoDni.getText());

            // compruebo si se ha encontrado
            if (clienteEncontrado == null) {
                etiquetaClienteEncontrado.setText("Cliente no encontrado");
                crearWarning("Cliente no encontrado", "No existe ningún cliente con ese DNI");
            } else {
                etiquetaClienteEncontrado.setText(clienteEncontrado.getNombre() + " " + clienteEncontrado.getApellidos());
            }
        });

        botonFormInvitados.setOnAction(event ->
                viewLoader.cargarVista("formInvitado-view.fxml", "Formulario de invitados"));

        botonAnadirInvitado.setOnAction(event -> {
            crearInformation("Pendiente", "La gestión de invitados se implementará más adelante");
        });

        botonQuitarInvitado.setOnAction(event -> {
            // compruebo si hay invitado seleccionado
            Invitado invitadoSeleccionado = listViewInvitados.getSelectionModel().getSelectedItem();

            if (invitadoSeleccionado == null) {
                crearWarning("Sin selección", "Debes seleccionar un invitado");
                return;
            }

            // elimino el invitado de la lista visible
            listaInvitados.remove(invitadoSeleccionado);
            spinnerNumeroInvitados.getValueFactory().setValue(listaInvitados.size());
        });
    }

    private void limpiarFormulario() {
        // limpio la referencia de trabajo
        reservaSeleccionada = null;
        clienteEncontrado = null;

        // limpio campos de texto
        campoTextoDni.clear();
        campoHora.clear();
        campoBusquedaInvitado.clear();
        campoVideo.clear();
        areaDescripcionPersonalizacion.clear();

        // limpio etiquetas
        etiquetaClienteEncontrado.setText("Cliente no seleccionado");

        // limpio selecciones
        selectorPack.getSelectionModel().clearSelection();
        selectorSala.getSelectionModel().clearSelection();
        selectorSupervisor.getSelectionModel().clearSelection();
        selectorEstadoReserva.getSelectionModel().clearSelection();
        selectorEstadoPersonalizacion.getSelectionModel().clearSelection();
        selectorFecha.setValue(null);

        // limpio check
        checkConfirmada.setSelected(false);

        // limpio lista invitados
        listaInvitados.clear();

        // reinicio spinner
        spinnerNumeroInvitados.getValueFactory().setValue(0);
    }

    public void cargarReserva(Reserva reserva) {
        // guardo la reserva recibida
        reservaSeleccionada = reserva;

        // compruebo que exista
        if (reservaSeleccionada == null) {
            return;
        }

        // cargo el cliente si existe
        if (reservaSeleccionada.getCliente() != null) {
            clienteEncontrado = reservaSeleccionada.getCliente();
            campoTextoDni.setText(clienteEncontrado.getDni());
            etiquetaClienteEncontrado.setText(clienteEncontrado.getNombre() + " " + clienteEncontrado.getApellidos());
        } else {
            clienteEncontrado = null;
            campoTextoDni.clear();
            etiquetaClienteEncontrado.setText("Cliente no seleccionado");
        }

        // cargo fecha y hora
        selectorFecha.setValue(reservaSeleccionada.getFecha());

        if (reservaSeleccionada.getHora() != null) {
            campoHora.setText(reservaSeleccionada.getHora().toString());
        } else {
            campoHora.clear();
        }

        // cargo pack, sala y supervisor
        selectorPack.setValue(reservaSeleccionada.getPack());
        selectorSala.setValue(reservaSeleccionada.getSala());
        selectorSupervisor.setValue(reservaSeleccionada.getSupervisor());

        // cargo estado y confirmación
        selectorEstadoReserva.setValue(reservaSeleccionada.getEstado());
        checkConfirmada.setSelected(reservaSeleccionada.isEsConfirmado());
    }
}