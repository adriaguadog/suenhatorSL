package org.example.suenhator.dao;

import org.example.suenhator.database.DBConnection;
import org.example.suenhator.database.SchemDB;
import org.example.suenhator.model.Supervisor;
import org.example.suenhator.utils.AlertCreation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupervisorDAO {

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public SupervisorDAO() {
        connection = DBConnection.getConnection();
    }

    public ArrayList<Supervisor> obtenerSupervisores() {
        ArrayList<Supervisor> listaSupervisores = new ArrayList<>();

        String query = String.format(
                "SELECT * FROM %s ORDER BY %s, %s",
                SchemDB.TAB_SUPERVISOR,
                SchemDB.COL_SUPERVISOR_APELLIDOS,
                SchemDB.COL_SUPERVISOR_NOMBRE
        );

        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                Supervisor supervisor = new Supervisor();
                supervisor.setIdSupervisor(rs.getInt(SchemDB.COL_SUPERVISOR_ID));
                supervisor.setNombre(rs.getString(SchemDB.COL_SUPERVISOR_NOMBRE));
                supervisor.setApellidos(rs.getString(SchemDB.COL_SUPERVISOR_APELLIDOS));
                supervisor.setDni(rs.getString(SchemDB.COL_SUPERVISOR_DNI));
                supervisor.setTelefono(rs.getString(SchemDB.COL_SUPERVISOR_TELEFONO));
                supervisor.setEmail(rs.getString(SchemDB.COL_SUPERVISOR_EMAIL));
                listaSupervisores.add(supervisor);
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return listaSupervisores;
    }

    public Supervisor registrarSupervisor(String nombre, String apellidos, String dni, String telefono, String email) {
        if (nombre == null || nombre.isBlank()
                || apellidos == null || apellidos.isBlank()
                || dni == null || dni.isBlank()) {
            return null;
        }

        String query = String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
                SchemDB.TAB_SUPERVISOR,
                SchemDB.COL_SUPERVISOR_NOMBRE,
                SchemDB.COL_SUPERVISOR_APELLIDOS,
                SchemDB.COL_SUPERVISOR_DNI,
                SchemDB.COL_SUPERVISOR_TELEFONO,
                SchemDB.COL_SUPERVISOR_EMAIL
        );

        try {
            ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombre.trim());
            ps.setString(2, apellidos.trim());
            ps.setString(3, dni.trim());
            ps.setString(4, telefono != null ? telefono.trim() : "");
            ps.setString(5, email != null ? email.trim() : "");

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    Supervisor supervisor = new Supervisor();
                    supervisor.setIdSupervisor(rs.getInt(1));
                    supervisor.setNombre(nombre.trim());
                    supervisor.setApellidos(apellidos.trim());
                    supervisor.setDni(dni.trim());
                    supervisor.setTelefono(telefono != null ? telefono.trim() : "");
                    supervisor.setEmail(email != null ? email.trim() : "");
                    return supervisor;
                }
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return null;
    }

    public boolean eliminarSupervisor(Supervisor supervisor) {
        if (supervisor == null || supervisor.getIdSupervisor() <= 0) {
            return false;
        }

        String query = String.format(
                "DELETE FROM %s WHERE %s = ?",
                SchemDB.TAB_SUPERVISOR,
                SchemDB.COL_SUPERVISOR_ID
        );

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, supervisor.getIdSupervisor());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return false;
    }

    public Supervisor buscarSupervisorPorDni(String dni) {
        if (dni == null || dni.isBlank()) {
            return null;
        }

        String query = String.format(
                "SELECT * FROM %s WHERE %s = ?",
                SchemDB.TAB_SUPERVISOR,
                SchemDB.COL_SUPERVISOR_DNI
        );

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, dni.trim());

            rs = ps.executeQuery();

            if (rs.next()) {
                Supervisor supervisor = new Supervisor();
                supervisor.setIdSupervisor(rs.getInt(SchemDB.COL_SUPERVISOR_ID));
                supervisor.setNombre(rs.getString(SchemDB.COL_SUPERVISOR_NOMBRE));
                supervisor.setApellidos(rs.getString(SchemDB.COL_SUPERVISOR_APELLIDOS));
                supervisor.setDni(rs.getString(SchemDB.COL_SUPERVISOR_DNI));
                supervisor.setTelefono(rs.getString(SchemDB.COL_SUPERVISOR_TELEFONO));
                supervisor.setEmail(rs.getString(SchemDB.COL_SUPERVISOR_EMAIL));
                return supervisor;
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return null;
    }
}