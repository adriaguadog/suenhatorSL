package org.example.suenhator.controller;

import controller.ReservaController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Sala;
import model.Supervisor;
import org.example.suenhator.data.Dataset;

import java.net.URL;
import java.util.ResourceBundle;

public class RecursosViewController implements Initializable {


    @FXML
    private ListView<Sala> listViewSalas;
    //lista asociada
    private ObservableList<Sala> listaSalas;

    @FXML
    private ListView<Supervisor> listViewSupervisores;
    //podria usar la lista directamente del dataset ya que no la voy a modificar
    private ObservableList<Supervisor> listaSupervisores;

    @FXML
    private TabPane panelPestanasRecursos;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        listaSupervisores= FXCollections.observableArrayList(Dataset.listaSupervisores);
        listaSalas= FXCollections.observableArrayList(Dataset.listaSalas);
    }

    private void initGUI() {
listViewSupervisores.setItems(listaSupervisores);
listViewSalas.setItems(listaSalas);
    }

    private void actions() {

    }
}