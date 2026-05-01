package org.example.suenhator.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.Locale;
import org.example.suenhator.model.Pack;

public class PacksViewController {

    @FXML
    private ListView<Pack> listViewPacks;

    @FXML
    private Label etiquetaNombrePack;

    @FXML
    private Label etiquetaTipoPack;

    @FXML
    private Label etiquetaDuracionPack;

    @FXML
    private Label etiquetaPrecioPack;

    @FXML
    private Label etiquetaAforoPack;

    @FXML
    private Label etiquetaPackPremium;

    @FXML
    private Label etiquetaPackMayoresEdad;

    @FXML
    private Label etiquetaDescripcionPack;

    @FXML
    public void initialize() {
        cargarPacks();
        configurarSeleccion();
        limpiarDetalle();
    }

    private void cargarPacks() {
        listViewPacks.setItems(FXCollections.observableArrayList(obtenerPacksActuales()));
    }

    private void configurarSeleccion() {
        listViewPacks.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            if (actual != null) {
                mostrarDetalle(actual);
            } else {
                limpiarDetalle();
            }
        });

        if (!listViewPacks.getItems().isEmpty()) {
            listViewPacks.getSelectionModel().selectFirst();
        }
    }

    private void mostrarDetalle(Pack pack) {
        etiquetaNombrePack.setText(valorTexto(pack.getNombre(), "Sin nombre"));
        etiquetaTipoPack.setText("Tipo: " + valorTexto(pack.getTipoPack(), "No disponible"));
        etiquetaDuracionPack.setText("Duración: " + pack.getDuracion() + " min");
        etiquetaPrecioPack.setText(String.format(Locale.US, "Precio: %.2f €", pack.getPrecio()));
        etiquetaAforoPack.setText("Aforo: " + pack.getAforo() + " personas");
        etiquetaPackPremium.setText("Premium: " + (pack.isPremium() ? "Sí" : "No"));
        etiquetaPackMayoresEdad.setText("Solo +18: " + (pack.isMas18() ? "Sí" : "No"));
        etiquetaDescripcionPack.setText(valorTexto(pack.getDescripcion(), "Sin descripción disponible."));
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

    private String valorTexto(String texto, String porDefecto) {
        return (texto == null || texto.isBlank()) ? porDefecto : texto;
    }

    private List<Pack> obtenerPacksActuales() {
        return List.of(
                new Pack(
                        "Pack Básico",
                        "Para empezar sin liarla demasiado. Perfecto para una primera experiencia.",
                        "basico",
                        40,
                        39.99,
                        false,
                        1,
                        false
                ),
                new Pack(
                        "Pack Aventura",
                        "Para quienes quieren algo más intenso y surrealista: playas paradisíacas, aventuras espaciales o terrorífica casa embrujada.",
                        "aventura",
                        60,
                        49.99,
                        false,
                        2,
                        false
                ),
                new Pack(
                        "Pack Trauma",
                        "Ideal para enfrentarte a tus miedos: aracnofobia, dejar de fumar, hablar en público o miedo a las alturas. A partir de 15 años.",
                        "trauma",
                        90,
                        55.00,
                        false,
                        2,
                        false
                ),
                new Pack(
                        "Pack Premium",
                        "Vive tus fantasías más raras con acabado deluxe y personajes personalizables. Incluye la opción de adaptar hasta 2 personajes.",
                        "premium",
                        90,
                        99.99,
                        true,
                        3,
                        true
                )
        );
    }
}