package org.example.suenhator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Pack;
import org.example.suenhator.utils.ViewLoader;

import java.net.URL;
import java.util.ResourceBundle;

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