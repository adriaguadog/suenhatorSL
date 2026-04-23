package org.example.suenhator.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

import static org.example.suenhator.utils.AlertCreation.crearError;

public final class ViewLoader {

    @Getter
    @Setter
    private static StackPane panelContenedorContenido;

    public ViewLoader() {
    }

    public static void cargarVista(String nombreVista, String titulo) {
        if (panelContenedorContenido == null) {
            crearError("Error", "El panel contenedor principal no está inicializado");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    ViewLoader.class.getResource("/org/example/suenhator/" + nombreVista)
            );

            Node vista = loader.load();
            panelContenedorContenido.getChildren().setAll(vista);

            Stage stage = (Stage) panelContenedorContenido.getScene().getWindow();
            stage.setTitle(titulo);

        } catch (IOException e) {
            crearError("Error", "No se ha encontrado el recurso: " + nombreVista);
        }
    }
}
