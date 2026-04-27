package org.example.suenhator.dao;

import org.example.suenhator.database.DBConnection;
import org.example.suenhator.database.SchemDB;
import org.example.suenhator.model.Sala;
import org.example.suenhator.utils.AlertCreation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SalaDAO {

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public SalaDAO() {
        connection = DBConnection.getConnection();
    }

    public ArrayList<Sala> obtenerSalas() {
        ArrayList<Sala> listaSalas = new ArrayList<>();

        String query = String.format(
                "SELECT * FROM %s ORDER BY %s",
                SchemDB.TAB_SALA,
                SchemDB.COL_SALA_NOMBRE
        );

        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                Sala sala = new Sala();
                sala.setIdSala(rs.getInt(SchemDB.COL_SALA_ID));
                sala.setNombre(rs.getString(SchemDB.COL_SALA_NOMBRE));
                sala.setCapacidad(rs.getInt(SchemDB.COL_SALA_CAPACIDAD));
                listaSalas.add(sala);
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return listaSalas;
    }

    public Sala registrarSala(String nombre, int capacidad) {
        if (nombre == null || nombre.isBlank() || capacidad <= 0) {
            return null;
        }

        String query = String.format(
                "INSERT INTO %s (%s, %s) VALUES (?, ?)",
                SchemDB.TAB_SALA,
                SchemDB.COL_SALA_NOMBRE,
                SchemDB.COL_SALA_CAPACIDAD
        );

        try {
            ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombre.trim());
            ps.setInt(2, capacidad);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    Sala sala = new Sala();
                    sala.setIdSala(rs.getInt(1));
                    sala.setNombre(nombre.trim());
                    sala.setCapacidad(capacidad);
                    return sala;
                }
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return null;
    }

    public boolean eliminarSala(Sala sala) {
        if (sala == null || sala.getIdSala() <= 0) {
            return false;
        }

        String query = String.format(
                "DELETE FROM %s WHERE %s = ?",
                SchemDB.TAB_SALA,
                SchemDB.COL_SALA_ID
        );

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, sala.getIdSala());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return false;
    }

    public Sala buscarSalaPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return null;
        }

        String query = String.format(
                "SELECT * FROM %s WHERE %s = ?",
                SchemDB.TAB_SALA,
                SchemDB.COL_SALA_NOMBRE
        );

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, nombre.trim());

            rs = ps.executeQuery();

            if (rs.next()) {
                Sala sala = new Sala();
                sala.setIdSala(rs.getInt(SchemDB.COL_SALA_ID));
                sala.setNombre(rs.getString(SchemDB.COL_SALA_NOMBRE));
                sala.setCapacidad(rs.getInt(SchemDB.COL_SALA_CAPACIDAD));
                return sala;
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return null;
    }
}