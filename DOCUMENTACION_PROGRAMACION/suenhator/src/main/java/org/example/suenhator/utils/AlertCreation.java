package org.example.suenhator.utils;

import javafx.scene.control.Alert;

public class AlertCreation {

    public AlertCreation() {
    }

    public static void crearWarning(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING); //ventana de dialogo
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void crearInformation(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); //ventana de dialogo
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void crearError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR); //ventana de dialogo
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

