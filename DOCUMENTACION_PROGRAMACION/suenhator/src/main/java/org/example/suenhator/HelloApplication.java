package org.example.suenhator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.suenhator.data.Dataset;
import java.io.IOException;

public class HelloApplication extends Application {

        @Getter
        //creo una sola vez la variable dataset accesible desde cualquier clase
        private static final Dataset dataset = new Dataset();

    @Override
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
            stage.setTitle("Sueñhator");
            stage.setScene(scene);
            stage.show();
        }


    public static void main(String[] args) {
        launch();
    }
}