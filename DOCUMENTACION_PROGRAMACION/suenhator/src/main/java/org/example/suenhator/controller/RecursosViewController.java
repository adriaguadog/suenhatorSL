package org.example.suenhator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.suenhator.utils.ViewLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class RecursosViewController implements Initializable {


    @FXML
    private ListView<?> listaSalasDisponibles;

    @FXML
    private ListView<?> listaSupervisoresDisponibles;

    @FXML
    private TabPane panelPestanasRecursos;

    private ViewLoader viewLoader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        viewLoader = new ViewLoader();
    }

    private void initGUI() {

    }

    private void actions() {

    }
}