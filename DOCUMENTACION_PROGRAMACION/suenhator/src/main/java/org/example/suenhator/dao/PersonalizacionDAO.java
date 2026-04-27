package org.example.suenhator.dao;

import org.example.suenhator.database.DBConnection;
import org.example.suenhator.database.SchemDB;
import org.example.suenhator.model.Personalizacion;
import org.example.suenhator.model.Reserva;
import org.example.suenhator.model.enums.EstadoPersonalizacion;
import org.example.suenhator.utils.AlertCreation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonalizacionDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public PersonalizacionDAO() {
        connection = DBConnection.getConnection();
    }

    public Personalizacion registrarPersonalizacion(Personalizacion personalizacion) {
        if (personalizacion == null || personalizacion.getReserva() == null) {
            return null;
        }

        String query = String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
                SchemDB.TAB_PERSONALIZACION,
                SchemDB.COL_PERSONALIZACION_VIDEO_REF,
                SchemDB.COL_PERSONALIZACION_DESCRIPCION,
                SchemDB.COL_PERSONALIZACION_FECHA_SOLICITUD,
                SchemDB.COL_PERSONALIZACION_FECHA_APROBACION,
                SchemDB.COL_PERSONALIZACION_ESTADO,
                SchemDB.COL_PERSONALIZACION_ID_RESERVA
        );

        try {
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, personalizacion.getVideoRef());
            preparedStatement.setString(2, personalizacion.getDescripcion());

            if (personalizacion.getFechaSolicitud() != null) {
                preparedStatement.setDate(3, Date.valueOf(personalizacion.getFechaSolicitud()));
            } else {
                preparedStatement.setDate(3, null);
            }

            if (personalizacion.getFechaAprobacion() != null) {
                preparedStatement.setDate(4, Date.valueOf(personalizacion.getFechaAprobacion()));
            } else {
                preparedStatement.setDate(4, null);
            }

            if (personalizacion.getEstado() != null) {
                preparedStatement.setString(5, personalizacion.getEstado().name());
            } else {
                preparedStatement.setString(5, null);
            }

            preparedStatement.setInt(6, personalizacion.getReserva().getIdReserva());

            int resultado = preparedStatement.executeUpdate();

            if (resultado > 0) {
                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    personalizacion.setIdPersonalizacion(resultSet.getInt(1));
                    return personalizacion;
                }
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return null;
    }

    public Personalizacion obtenerPersonalizacionPorReserva(Reserva reserva) {
        if (reserva == null) {
            return null;
        }

        String query = String.format(
                "SELECT * FROM %s WHERE %s = ?",
                SchemDB.TAB_PERSONALIZACION,
                SchemDB.COL_PERSONALIZACION_ID_RESERVA
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reserva.getIdReserva());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Personalizacion personalizacion = new Personalizacion();
                personalizacion.setIdPersonalizacion(resultSet.getInt(SchemDB.COL_PERSONALIZACION_ID));
                personalizacion.setReserva(reserva);
                personalizacion.setVideoRef(resultSet.getString(SchemDB.COL_PERSONALIZACION_VIDEO_REF));
                personalizacion.setDescripcion(resultSet.getString(SchemDB.COL_PERSONALIZACION_DESCRIPCION));

                Date fechaSolicitud = resultSet.getDate(SchemDB.COL_PERSONALIZACION_FECHA_SOLICITUD);
                if (fechaSolicitud != null) {
                    personalizacion.setFechaSolicitud(fechaSolicitud.toLocalDate());
                }

                Date fechaAprobacion = resultSet.getDate(SchemDB.COL_PERSONALIZACION_FECHA_APROBACION);
                if (fechaAprobacion != null) {
                    personalizacion.setFechaAprobacion(fechaAprobacion.toLocalDate());
                }

                String estado = resultSet.getString(SchemDB.COL_PERSONALIZACION_ESTADO);
                if (estado != null) {
                    personalizacion.setEstado(EstadoPersonalizacion.valueOf(estado));
                }

                return personalizacion;
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return null;
    }

    public boolean actualizarPersonalizacion(Personalizacion personalizacion) {
        if (personalizacion == null) {
            return false;
        }

        String query = String.format(
                "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
                SchemDB.TAB_PERSONALIZACION,
                SchemDB.COL_PERSONALIZACION_VIDEO_REF,
                SchemDB.COL_PERSONALIZACION_DESCRIPCION,
                SchemDB.COL_PERSONALIZACION_FECHA_SOLICITUD,
                SchemDB.COL_PERSONALIZACION_FECHA_APROBACION,
                SchemDB.COL_PERSONALIZACION_ESTADO,
                SchemDB.COL_PERSONALIZACION_ID
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, personalizacion.getVideoRef());
            preparedStatement.setString(2, personalizacion.getDescripcion());

            if (personalizacion.getFechaSolicitud() != null) {
                preparedStatement.setDate(3, Date.valueOf(personalizacion.getFechaSolicitud()));
            } else {
                preparedStatement.setDate(3, null);
            }

            if (personalizacion.getFechaAprobacion() != null) {
                preparedStatement.setDate(4, Date.valueOf(personalizacion.getFechaAprobacion()));
            } else {
                preparedStatement.setDate(4, null);
            }

            if (personalizacion.getEstado() != null) {
                preparedStatement.setString(5, personalizacion.getEstado().name());
            } else {
                preparedStatement.setString(5, null);
            }

            preparedStatement.setInt(6, personalizacion.getIdPersonalizacion());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return false;
    }

    public boolean eliminarPersonalizacion(Personalizacion personalizacion) {
        if (personalizacion == null) {
            return false;
        }

        String query = String.format(
                "DELETE FROM %s WHERE %s = ?",
                SchemDB.TAB_PERSONALIZACION,
                SchemDB.COL_PERSONALIZACION_ID
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, personalizacion.getIdPersonalizacion());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return false;
    }
}