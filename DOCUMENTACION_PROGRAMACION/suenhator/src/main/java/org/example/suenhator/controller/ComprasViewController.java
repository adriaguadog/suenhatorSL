package org.example.suenhator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.suenhator.dao.ClienteDAO;
import org.example.suenhator.dao.CompraDAO;
import org.example.suenhator.dao.PackDAO;
import org.example.suenhator.model.Cliente;
import org.example.suenhator.model.Compra;
import org.example.suenhator.model.LineaCompra;
import org.example.suenhator.model.Pack;
import org.example.suenhator.model.Reserva;
import org.example.suenhator.model.enums.EstadoCompra;
import org.example.suenhator.utils.ViewLoader;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearInformation;
import static org.example.suenhator.utils.AlertCreation.crearWarning;

public class ComprasViewController implements Initializable {

    @FXML
    private Button botonBuscarClientePorDni;

    @FXML
    private VBox tarjetaDetalleCompra;

    @FXML
    private Button botonAnadirLineaCompra;

    @FXML
    private Button botonEliminarLineaCompra;

    @FXML
    private Button botonGuardarCompra;

    @FXML
    private Button botonCancelarCompra;

    @FXML
    private TextField campoDniClienteCompra;

    @FXML
    private Label etiquetaClienteCompraSeleccionado;

    @FXML
    private ComboBox<Pack> selectorPackCompra;
    private ObservableList<Pack> listaPacksCompra;

    @FXML
    private Spinner<Integer> selectorCantidadPackCompra;

    @FXML
    private ListView<LineaCompra> listViewLineasCompra;
    private ObservableList<LineaCompra> listaLineasCompra;

    @FXML
    private Label etiquetaTotalCompra;

    @FXML
    private ListView<Compra> listViewComprasCliente;
    private ObservableList<Compra> listaComprasCliente;

    private ClienteDAO clienteDAO;
    private CompraDAO compraDAO;
    private PackDAO packDAO;

    private Cliente clienteSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        clienteDAO = new ClienteDAO();
        compraDAO = new CompraDAO();
        packDAO = new PackDAO();

        listaPacksCompra = FXCollections.observableArrayList();
        listaLineasCompra = FXCollections.observableArrayList();
        listaComprasCliente = FXCollections.observableArrayList();
    }

    private void initGUI() {
        listaPacksCompra.addAll(packDAO.obtenerPacks());
        selectorPackCompra.setItems(listaPacksCompra);

        listViewLineasCompra.setItems(listaLineasCompra);
        listViewComprasCliente.setItems(listaComprasCliente);

        selectorCantidadPackCompra.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 1, 1)
        );

        etiquetaClienteCompraSeleccionado.setText("Ningún cliente seleccionado");
        etiquetaTotalCompra.setText("Total: 0,00 €");
    }

    private void actions() {
        botonBuscarClientePorDni.setOnAction(event -> buscarClienteYMostrarCompras());
        botonAnadirLineaCompra.setOnAction(event -> anadirLineaCompra());
        botonEliminarLineaCompra.setOnAction(event -> eliminarLineaCompra());
        botonGuardarCompra.setOnAction(event -> registrarCompra());
        listViewComprasCliente.getSelectionModel().selectedItemProperty().addListener((observable, oldCompra, nuevaCompra) -> actualizarEstadoPanelCompra(nuevaCompra));
    }

    private void buscarClienteYMostrarCompras() {
        String dni = campoDniClienteCompra.getText();

        if (dni == null || dni.isBlank()) {
            crearWarning("DNI vacío", "Debes introducir un DNI");
            return;
        }

        clienteSeleccionado = clienteDAO.buscarPorDni(dni.trim());

        if (clienteSeleccionado == null) {
            etiquetaClienteCompraSeleccionado.setText("Ningún cliente seleccionado");
            listaComprasCliente.clear();
            listaLineasCompra.clear();
            etiquetaTotalCompra.setText("Total: 0,00 €");
            listViewComprasCliente.refresh();
            crearWarning("Cliente no encontrado", "No existe ningún cliente con ese DNI");
            return;
        }

        etiquetaClienteCompraSeleccionado.setText(
                clienteSeleccionado.getNombre() + " " + clienteSeleccionado.getApellidos()
        );

        listaComprasCliente.clear();
        listaComprasCliente.addAll(compraDAO.obtenerComprasPorCliente(clienteSeleccionado));
        listaLineasCompra.clear();
        etiquetaTotalCompra.setText("Total: 0,00 €");
        listViewComprasCliente.refresh();
        actualizarEstadoPanelCompra(null);
    }

    private void anadirLineaCompra() {
        if (clienteSeleccionado == null) {
            crearWarning("Sin cliente", "Debes buscar un cliente antes de añadir líneas");
            return;
        }

        Pack packSeleccionado = selectorPackCompra.getValue();

        if (packSeleccionado == null) {
            crearWarning("Pack no seleccionado", "Debes seleccionar un pack");
            return;
        }

        int cantidad = selectorCantidadPackCompra.getValue();

        for (LineaCompra linea : listaLineasCompra) {
            if (linea.getPack().getIdPack() == packSeleccionado.getIdPack()) {
                int nuevaCantidad = linea.getCantidad() + cantidad;
                linea.setCantidad(nuevaCantidad);
                linea.setSubtotal(linea.getPrecioUnitario() * nuevaCantidad);
                listViewLineasCompra.refresh();
                actualizarEtiquetaTotal();
                return;
            }
        }

        double precioUnitario = packSeleccionado.getPrecio();
        double subtotal = precioUnitario * cantidad;

        listaLineasCompra.add(new LineaCompra(null, packSeleccionado, cantidad, precioUnitario, subtotal));
        actualizarEtiquetaTotal();
    }

    private void eliminarLineaCompra() {
        LineaCompra lineaSeleccionada = listViewLineasCompra.getSelectionModel().getSelectedItem();

        if (lineaSeleccionada == null) {
            crearWarning("Ninguna línea seleccionada", "Debes seleccionar una línea para eliminarla");
            return;
        }

        listaLineasCompra.remove(lineaSeleccionada);
        actualizarEtiquetaTotal();
    }

    private void registrarCompra() {
        if (clienteSeleccionado == null) {
            crearWarning("Sin cliente", "Debes buscar un cliente antes de registrar una compra");
            return;
        }

        if (listaLineasCompra.isEmpty()) {
            crearWarning("Sin líneas", "Debes añadir al menos una línea a la compra");
            return;
        }

        Compra nuevaCompra = compraDAO.registrarCompra(clienteSeleccionado);

        if (nuevaCompra == null) {
            crearWarning("Error", "No se pudo registrar la compra");
            return;
        }

        double total = 0.0;

        for (LineaCompra linea : listaLineasCompra) {
            boolean lineaGuardada = compraDAO.registrarLineaCompra(nuevaCompra, linea);

            if (!lineaGuardada) {
                crearWarning("Error", "No se pudo registrar una línea de la compra");
                return;
            }

            total += linea.getSubtotal();
        }

        boolean totalActualizado = compraDAO.actualizarTotalCompra(nuevaCompra, total);

        if (!totalActualizado) {
            crearWarning("Error", "La compra se creó, pero no se pudo actualizar el total");
            return;
        }

        nuevaCompra.setTotal(total);

        listaComprasCliente.clear();
        listaComprasCliente.addAll(compraDAO.obtenerComprasPorCliente(clienteSeleccionado));

        listaLineasCompra.clear();
        etiquetaTotalCompra.setText("Total: 0,00 €");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Compra registrada");
        alert.setHeaderText("La compra se ha guardado correctamente");
        alert.setContentText("¿Deseas realizar el pago ahora?");

        ButtonType botonIrAPago = new ButtonType("Ir a pago");
        ButtonType botonDejarPendiente = new ButtonType("Dejar pendiente");
        alert.getButtonTypes().setAll(botonIrAPago, botonDejarPendiente);

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonIrAPago) {
            abrirVistaPagoConCompra(nuevaCompra);
        } else {
            crearInformation("Compra pendiente", "La compra ha quedado registrada como pendiente de pago");
        }
    }
    private void actualizarEstadoPanelCompra(Compra compraSeleccionada) {
        if (compraSeleccionada == null) {
            tarjetaDetalleCompra.setDisable(false);
            botonGuardarCompra.setDisable(false);
            return;
        }

        if (compraSeleccionada.getEstado() == EstadoCompra.pendiente) {
            tarjetaDetalleCompra.setDisable(false);
            botonGuardarCompra.setDisable(false);
        } else {
            tarjetaDetalleCompra.setDisable(true);
            botonGuardarCompra.setDisable(true);
        }
    }
    private void abrirVistaPagoConCompra(Compra compra) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/suenhator/pagos-view.fxml")
            );
            Node vista = loader.load();

            PagosViewController controller = loader.getController();
            controller.cargarCompra(compra);

            ViewLoader.cargarVista(vista, "Pagos");
        } catch (Exception e) {
            e.printStackTrace();
            crearWarning("Error", "La compra se registró, pero no se pudo abrir la vista de pagos");
        }
    }

    private void actualizarEtiquetaTotal() {
        double total = listaLineasCompra.stream()
                .mapToDouble(LineaCompra::getSubtotal)
                .sum();

        etiquetaTotalCompra.setText(String.format("Total: %.2f €", total).replace(".", ","));
    }

    public void cargarCompraDesdeReserva(Reserva reserva) {
        if (reserva == null || reserva.getCliente() == null || reserva.getPack() == null) {
            return;
        }

        clienteSeleccionado = clienteDAO.buscarPorDni(reserva.getCliente().getDni());

        if (clienteSeleccionado == null) {
            clienteSeleccionado = reserva.getCliente();
        }

        campoDniClienteCompra.setText(clienteSeleccionado.getDni());
        etiquetaClienteCompraSeleccionado.setText(
                clienteSeleccionado.getNombre() + " " + clienteSeleccionado.getApellidos()
        );

        listaComprasCliente.clear();
        listaComprasCliente.addAll(compraDAO.obtenerComprasPorCliente(clienteSeleccionado));

        listaLineasCompra.clear();
        listViewComprasCliente.getSelectionModel().clearSelection();
        actualizarEstadoPanelCompra(null);

        Pack packReserva = null;
        for (Pack pack : listaPacksCompra) {
            if (pack.getIdPack() == reserva.getPack().getIdPack()) {
                packReserva = pack;
                break;
            }
        }

        if (packReserva == null) {
            packReserva = reserva.getPack();
        }

        selectorPackCompra.setValue(packReserva);
        selectorCantidadPackCompra.getValueFactory().setValue(1);

        double precioUnitario = packReserva.getPrecio();
        listaLineasCompra.add(new LineaCompra(null, packReserva, 1, precioUnitario, precioUnitario));
        actualizarEtiquetaTotal();
    }
}