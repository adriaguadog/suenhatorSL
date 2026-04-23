package org.example.suenhator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Pack;
import org.example.suenhator.data.Dataset;

import java.net.URL;
import java.util.ResourceBundle;

import static org.example.suenhator.utils.AlertCreation.crearWarning;

public class PacksViewController implements Initializable {

    @FXML
    private Button botonVerDetallePack;

    @FXML
    private Label etiquetaAforoPack;

    @FXML
    private Label etiquetaDescripcionPack;

    @FXML
    private Label etiquetaDuracionPack;

    @FXML
    private Label etiquetaNombrePack;

    @FXML
    private Label etiquetaPackMayoresEdad;

    @FXML
    private Label etiquetaPackPremium;

    @FXML
    private Label etiquetaPrecioPack;

    @FXML
    private Label etiquetaTipoPack;

    @FXML
    private ListView<Pack> listaCatalogoPacks;

    private ObservableList<Pack> listaPacksMostrada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        listaPacksMostrada = FXCollections.observableArrayList();
        listaPacksMostrada.addAll(Dataset.listaPacks);
    }

    private void initGUI() {
        listaCatalogoPacks.setItems(listaPacksMostrada);
        limpiarDetalle();
    }

    private void actions() {
        botonVerDetallePack.setOnAction(event -> {
            Pack packSeleccionado = listaCatalogoPacks.getSelectionModel().getSelectedItem();

            if (packSeleccionado == null) {
                crearWarning("Sin selección", "Debes seleccionar un pack");
                return;
            }

            etiquetaNombrePack.setText(packSeleccionado.getNombre());
            etiquetaTipoPack.setText("Tipo: " + packSeleccionado.getTipoPack());
            etiquetaDuracionPack.setText("Duración: " + packSeleccionado.getDuracion());
            etiquetaPrecioPack.setText("Precio: " + packSeleccionado.getPrecio() + " €");
            etiquetaAforoPack.setText("Aforo: " + packSeleccionado.getAforo());
            etiquetaPackPremium.setText("Premium: " + (packSeleccionado.isPremium() ? "Sí" : "No"));
            etiquetaPackMayoresEdad.setText("Solo +18: " + (packSeleccionado.isMas18() ? "Sí" : "No"));
            etiquetaDescripcionPack.setText(packSeleccionado.getDescripcion());
        });
    }

    private void limpiarDetalle() {
        etiquetaNombrePack.setText("Selecciona un pack");
        etiquetaTipoPack.setText("Tipo:");
        etiquetaDuracionPack.setText("Duración:");
        etiquetaPrecioPack.setText("Precio:");
        etiquetaAforoPack.setText("Aforo:");
        etiquetaPackPremium.setText("Premium:");
        etiquetaPackMayoresEdad.setText("Solo +18:");
        etiquetaDescripcionPack.setText("");
    }
}
