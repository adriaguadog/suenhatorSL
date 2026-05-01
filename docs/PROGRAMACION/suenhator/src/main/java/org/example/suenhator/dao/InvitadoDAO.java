package org.example.suenhator.dao;

import org.example.suenhator.database.DBConnection;
import org.example.suenhator.database.SchemDB;
import org.example.suenhator.model.Invitado;
import org.example.suenhator.model.Reserva;
import org.example.suenhator.utils.AlertCreation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InvitadoDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public InvitadoDAO() {
        connection = DBConnection.getConnection();
    }

    public Invitado registrarInvitado(Invitado invitado) {
        if (invitado == null) {
            return null;
        }

        String query = String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
                SchemDB.TAB_INVITADO,
                SchemDB.COL_INVITADO_NOMBRE,
                SchemDB.COL_INVITADO_APELLIDOS,
                SchemDB.COL_INVITADO_DNI,
                SchemDB.COL_INVITADO_TELEFONO,
                SchemDB.COL_INVITADO_EMAIL,
                SchemDB.COL_INVITADO_FECHA_NAC
        );

        try {
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, invitado.getNombre());
            preparedStatement.setString(2, invitado.getApellidos());
            preparedStatement.setString(3, invitado.getDni());
            preparedStatement.setString(4, invitado.getTelefono());
            preparedStatement.setString(5, invitado.getEmail());

            if (invitado.getFechaNac() != null) {
                preparedStatement.setDate(6, Date.valueOf(invitado.getFechaNac()));
            } else {
                preparedStatement.setDate(6, null);
            }

            int resultado = preparedStatement.executeUpdate();

            if (resultado > 0) {
                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    invitado.setIdInvitado(resultSet.getInt(1));
                    return invitado;
                }
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return null;
    }

    public boolean vincularInvitadoAReserva(Reserva reserva, Invitado invitado, boolean esConfirmado) {
        if (reserva == null || invitado == null) {
            return false;
        }

        String query = String.format(
                "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
                SchemDB.TAB_RESERVA_INVITADO,
                SchemDB.COL_RESERVA_INVITADO_ES_CONFIRMADO,
                SchemDB.COL_RESERVA_INVITADO_ID_RESERVA,
                SchemDB.COL_RESERVA_INVITADO_ID_INVITADO
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, esConfirmado);
            preparedStatement.setInt(2, reserva.getIdReserva());
            preparedStatement.setInt(3, invitado.getIdInvitado());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return false;
    }

    public Invitado registrarInvitadoEnReserva(Reserva reserva, Invitado invitado, boolean esConfirmado) {
        if (reserva == null || invitado == null) {
            return null;
        }

        Invitado invitadoRegistrado = registrarInvitado(invitado);

        if (invitadoRegistrado == null) {
            return null;
        }

        boolean vinculado = vincularInvitadoAReserva(reserva, invitadoRegistrado, esConfirmado);

        if (vinculado) {
            return invitadoRegistrado;
        }

        return null;
    }

    public ArrayList<Invitado> obtenerInvitadosPorReserva(Reserva reserva) {
        ArrayList<Invitado> listaInvitados = new ArrayList<>();

        if (reserva == null) {
            return listaInvitados;
        }

        String query = String.format(
                "SELECT i.*, ri.%s FROM %s i " +
                        "INNER JOIN %s ri ON i.%s = ri.%s " +
                        "WHERE ri.%s = ?",
                SchemDB.COL_RESERVA_INVITADO_ES_CONFIRMADO,
                SchemDB.TAB_INVITADO,
                SchemDB.TAB_RESERVA_INVITADO,
                SchemDB.COL_INVITADO_ID,
                SchemDB.COL_RESERVA_INVITADO_ID_INVITADO,
                SchemDB.COL_RESERVA_INVITADO_ID_RESERVA
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reserva.getIdReserva());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Invitado invitado = new Invitado();
                invitado.setIdInvitado(resultSet.getInt(SchemDB.COL_INVITADO_ID));
                invitado.setNombre(resultSet.getString(SchemDB.COL_INVITADO_NOMBRE));
                invitado.setApellidos(resultSet.getString(SchemDB.COL_INVITADO_APELLIDOS));
                invitado.setDni(resultSet.getString(SchemDB.COL_INVITADO_DNI));
                invitado.setTelefono(resultSet.getString(SchemDB.COL_INVITADO_TELEFONO));
                invitado.setEmail(resultSet.getString(SchemDB.COL_INVITADO_EMAIL));

                Date fechaNac = resultSet.getDate(SchemDB.COL_INVITADO_FECHA_NAC);
                if (fechaNac != null) {
                    invitado.setFechaNac(fechaNac.toLocalDate());
                }

                listaInvitados.add(invitado);
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return listaInvitados;
    }

    public boolean desvincularInvitadoDeReserva(Reserva reserva, Invitado invitado) {
        if (reserva == null || invitado == null) {
            return false;
        }

        String query = String.format(
                "DELETE FROM %s WHERE %s = ? AND %s = ?",
                SchemDB.TAB_RESERVA_INVITADO,
                SchemDB.COL_RESERVA_INVITADO_ID_RESERVA,
                SchemDB.COL_RESERVA_INVITADO_ID_INVITADO
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reserva.getIdReserva());
            preparedStatement.setInt(2, invitado.getIdInvitado());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return false;
    }

    public boolean eliminarInvitado(Invitado invitado) {
        if (invitado == null) {
            return false;
        }

        String query = String.format(
                "DELETE FROM %s WHERE %s = ?",
                SchemDB.TAB_INVITADO,
                SchemDB.COL_INVITADO_ID
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, invitado.getIdInvitado());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return false;
    }
}