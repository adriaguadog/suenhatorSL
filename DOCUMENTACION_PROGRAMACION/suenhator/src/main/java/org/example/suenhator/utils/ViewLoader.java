package org.example.suenhator.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.suenhator.HelloApplication;

import java.io.IOException;

import static org.example.suenhator.utils.AlertCreation.crearError;

//final porque nadie va a heredar de ella
public final class ViewLoader{


    public ViewLoader() {
    }

//static:accesibles desde cualquier clase sin crear objeto
    public static void cargarVista(String recurso, Button boton, String titulo) {
        Stage stage=new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(recurso));
            Scene scene = new Scene(loader.load(),880, 640);
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
            ((Stage) boton.getScene().getWindow()).close();
        } catch ( IOException e) {
            crearError("Error", "No se ha encontrado el recurso");
        }
    }
}