package org.example.suenhator.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Cliente;
import model.Pack;
import model.Reserva;
import model.ReservaInvitado;
import model.Sala;
import model.Supervisor;


public class Dataset {

    //listas compartidas accesibles sin crear objeto
    public static ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    public static ObservableList<Reserva> listaReservas = FXCollections.observableArrayList();
    public static ObservableList<Pack> listaPacks = FXCollections.observableArrayList();
    public static ObservableList<Sala> listaSalas = FXCollections.observableArrayList();
    public static ObservableList<Supervisor> listaSupervisores = FXCollections.observableArrayList();
    public static ObservableList<ReservaInvitado> listaReservasInvitados = FXCollections.observableArrayList();
}