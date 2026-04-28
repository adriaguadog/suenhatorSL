package org.example.suenhator.dao;

import org.example.suenhator.database.DBConnection;
import org.example.suenhator.database.SchemDB;
import org.example.suenhator.model.Cliente;
import org.example.suenhator.model.Pack;
import org.example.suenhator.model.Reserva;
import org.example.suenhator.model.Sala;
import org.example.suenhator.model.Supervisor;
import org.example.suenhator.model.enums.EstadoReserva;
import org.example.suenhator.utils.AlertCreation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ReservaDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public ReservaDAO() {
        connection = DBConnection.getConnection();
    }

    public ArrayList<Pack> listarPacks() {
        ArrayList<Pack> listaPacks = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", SchemDB.TAB_PACK);

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Pack pack = new Pack();
                pack.setIdPack(resultSet.getInt(SchemDB.COL_PACK_ID));
                pack.setNombre(resultSet.getString(SchemDB.COL_PACK_NOMBRE));
                pack.setDescripcion(resultSet.getString(SchemDB.COL_PACK_DESCRIPCION));
                pack.setTipoPack(resultSet.getString(SchemDB.COL_PACK_TIPO_PACK));
                pack.setDuracion(resultSet.getInt(SchemDB.COL_PACK_DURACION));
                pack.setPrecio(resultSet.getDouble(SchemDB.COL_PACK_PRECIO));
                pack.setPremium(resultSet.getBoolean(SchemDB.COL_PACK_ES_PREMIUM));
                pack.setAforo(resultSet.getInt(SchemDB.COL_PACK_AFORO));
                pack.setMas18(resultSet.getBoolean(SchemDB.COL_PACK_ES_18));
                listaPacks.add(pack);
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return listaPacks;
    }

    public ArrayList<Supervisor> listarSupervisores() {
        ArrayList<Supervisor> listaSupervisores = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", SchemDB.TAB_SUPERVISOR);

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Supervisor supervisor = new Supervisor(
                        resultSet.getString(SchemDB.COL_SUPERVISOR_NOMBRE),
                        resultSet.getString(SchemDB.COL_SUPERVISOR_APELLIDOS),
                        resultSet.getString(SchemDB.COL_SUPERVISOR_DNI),
                        resultSet.getString(SchemDB.COL_SUPERVISOR_TELEFONO),
                        resultSet.getString(SchemDB.COL_SUPERVISOR_EMAIL)
                );
                supervisor.setIdSupervisor(resultSet.getInt(SchemDB.COL_SUPERVISOR_ID));
                listaSupervisores.add(supervisor);
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return listaSupervisores;
    }

    public Reserva crearReserva(Cliente cliente, Sala sala, Pack pack, Supervisor supervisor, LocalDate fecha, LocalTime hora, EstadoReserva estado) {
        Reserva reserva = null;

        if (cliente == null || sala == null || pack == null || supervisor == null || fecha == null || hora == null || estado == null) {
            return null;
        }

        String query = String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                SchemDB.TAB_RESERVA,
                SchemDB.COL_RESERVA_FECHA,
                SchemDB.COL_RESERVA_HORA,
                SchemDB.COL_RESERVA_ESTADO,
                SchemDB.COL_RESERVA_ES_CONFIRMADO,
                SchemDB.COL_RESERVA_ID_CLIENTE,
                SchemDB.COL_RESERVA_ID_SALA,
                SchemDB.COL_RESERVA_ID_PACK,
                SchemDB.COL_RESERVA_ID_SUPERVISOR
        );

        try {
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, Date.valueOf(fecha));
            preparedStatement.setTime(2, Time.valueOf(hora));
            preparedStatement.setString(3, estado.name().toLowerCase());
            preparedStatement.setBoolean(4, estado == EstadoReserva.confirmada);
            preparedStatement.setInt(5, cliente.getIdCliente());
            preparedStatement.setInt(6, sala.getIdSala());
            preparedStatement.setInt(7, pack.getIdPack());
            preparedStatement.setInt(8, supervisor.getIdSupervisor());

            int resultado = preparedStatement.executeUpdate();

            if (resultado > 0) {
                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    reserva = new Reserva();
                    reserva.setIdReserva(resultSet.getInt(1));
                    reserva.setCliente(cliente);
                    reserva.setSala(sala);
                    reserva.setPack(pack);
                    reserva.setSupervisor(supervisor);
                    reserva.setFecha(fecha);
                    reserva.setHora(hora);
                    reserva.setEstado(estado);
                    reserva.setEsConfirmado(estado == EstadoReserva.confirmada);
                }
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return reserva;
    }

    public boolean modificarReserva(int idReserva, Sala sala, Pack pack, Supervisor supervisor, LocalDate fecha, LocalTime hora, EstadoReserva estado) {
        if (idReserva <= 0 || sala == null || pack == null || supervisor == null || fecha == null || hora == null || estado == null) {
            return false;
        }

        String query = String.format(
                "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
                SchemDB.TAB_RESERVA,
                SchemDB.COL_RESERVA_FECHA,
                SchemDB.COL_RESERVA_HORA,
                SchemDB.COL_RESERVA_ESTADO,
                SchemDB.COL_RESERVA_ES_CONFIRMADO,
                SchemDB.COL_RESERVA_ID_SALA,
                SchemDB.COL_RESERVA_ID_PACK,
                SchemDB.COL_RESERVA_ID_SUPERVISOR,
                SchemDB.COL_RESERVA_ID
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(fecha));
            preparedStatement.setTime(2, Time.valueOf(hora));
            preparedStatement.setString(3, estado.name().toLowerCase());
            preparedStatement.setBoolean(4, estado == EstadoReserva.confirmada || estado == EstadoReserva.completada);
            preparedStatement.setInt(5, sala.getIdSala());
            preparedStatement.setInt(6, pack.getIdPack());
            preparedStatement.setInt(7, supervisor.getIdSupervisor());
            preparedStatement.setInt(8, idReserva);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return false;
    }

    public ArrayList<Reserva> consultarReservasPorFecha(LocalDate fecha) {
        ArrayList<Reserva> listaReservas = new ArrayList<>();

        if (fecha == null) {
            return listaReservas;
        }

        String query = """
                SELECT
                    r.*,
                    c.nombre     AS cliente_nombre,
                    c.apellidos  AS cliente_apellidos,
                    c.dni        AS cliente_dni,
                    p.id_pack    AS pack_id,
                    p.nombre     AS pack_nombre,
                    p.duracion   AS pack_duracion,
                    s.id_sala    AS sala_id,
                    s.nombre     AS sala_nombre,
                    sp.id_supervisor AS supervisor_id,
                    sp.nombre        AS supervisor_nombre
                FROM %s r
                INNER JOIN %s c  ON r.%s = c.%s
                INNER JOIN %s p  ON r.%s = p.%s
                INNER JOIN %s s  ON r.%s = s.%s
                INNER JOIN %s sp ON r.%s = sp.%s
                WHERE r.%s = ?
                ORDER BY r.%s, r.%s
                """.formatted(
                SchemDB.TAB_RESERVA,
                SchemDB.TAB_CLIENTE,
                SchemDB.COL_RESERVA_ID_CLIENTE, SchemDB.COL_CLIENTE_ID,
                SchemDB.TAB_PACK,
                SchemDB.COL_RESERVA_ID_PACK, SchemDB.COL_PACK_ID,
                SchemDB.TAB_SALA,
                SchemDB.COL_RESERVA_ID_SALA, SchemDB.COL_SALA_ID,
                SchemDB.TAB_SUPERVISOR,
                SchemDB.COL_RESERVA_ID_SUPERVISOR, SchemDB.COL_SUPERVISOR_ID,
                SchemDB.COL_RESERVA_FECHA,
                SchemDB.COL_RESERVA_HORA, SchemDB.COL_RESERVA_ID
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(fecha));
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listaReservas.add(mapearReservaCompleta());
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return listaReservas;
    }

    public ArrayList<Reserva> consultarReservasPorCliente(Cliente cliente) {
        ArrayList<Reserva> listaReservas = new ArrayList<>();

        if (cliente == null || cliente.getIdCliente() == 0) {
            return listaReservas;
        }

        String query = """
                SELECT
                    r.*,
                    c.nombre     AS cliente_nombre,
                    c.apellidos  AS cliente_apellidos,
                    c.dni        AS cliente_dni,
                    p.id_pack    AS pack_id,
                    p.nombre     AS pack_nombre,
                    p.duracion   AS pack_duracion,
                    s.id_sala    AS sala_id,
                    s.nombre     AS sala_nombre,
                    sp.id_supervisor AS supervisor_id,
                    sp.nombre        AS supervisor_nombre
                FROM %s r
                INNER JOIN %s c  ON r.%s = c.%s
                INNER JOIN %s p  ON r.%s = p.%s
                INNER JOIN %s s  ON r.%s = s.%s
                INNER JOIN %s sp ON r.%s = sp.%s
                WHERE r.%s = ?
                ORDER BY r.%s DESC, r.%s DESC
                """.formatted(
                SchemDB.TAB_RESERVA,
                SchemDB.TAB_CLIENTE,
                SchemDB.COL_RESERVA_ID_CLIENTE, SchemDB.COL_CLIENTE_ID,
                SchemDB.TAB_PACK,
                SchemDB.COL_RESERVA_ID_PACK, SchemDB.COL_PACK_ID,
                SchemDB.TAB_SALA,
                SchemDB.COL_RESERVA_ID_SALA, SchemDB.COL_SALA_ID,
                SchemDB.TAB_SUPERVISOR,
                SchemDB.COL_RESERVA_ID_SUPERVISOR, SchemDB.COL_SUPERVISOR_ID,
                SchemDB.COL_RESERVA_ID_CLIENTE,
                SchemDB.COL_RESERVA_FECHA, SchemDB.COL_RESERVA_HORA
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, cliente.getIdCliente());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listaReservas.add(mapearReservaCompleta());
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return listaReservas;
    }

    public ArrayList<Reserva> consultarReservasPorClienteYFecha(Cliente cliente, LocalDate fecha) {
        ArrayList<Reserva> listaReservas = new ArrayList<>();

        if (cliente == null || cliente.getIdCliente() == 0 || fecha == null) {
            return listaReservas;
        }

        String query = """
                SELECT
                    r.*,
                    c.nombre     AS cliente_nombre,
                    c.apellidos  AS cliente_apellidos,
                    c.dni        AS cliente_dni,
                    p.id_pack    AS pack_id,
                    p.nombre     AS pack_nombre,
                    p.duracion   AS pack_duracion,
                    s.id_sala    AS sala_id,
                    s.nombre     AS sala_nombre,
                    sp.id_supervisor AS supervisor_id,
                    sp.nombre        AS supervisor_nombre
                FROM %s r
                INNER JOIN %s c  ON r.%s = c.%s
                INNER JOIN %s p  ON r.%s = p.%s
                INNER JOIN %s s  ON r.%s = s.%s
                INNER JOIN %s sp ON r.%s = sp.%s
                WHERE r.%s = ? AND r.%s = ?
                ORDER BY r.%s DESC, r.%s DESC
                """.formatted(
                SchemDB.TAB_RESERVA,
                SchemDB.TAB_CLIENTE,
                SchemDB.COL_RESERVA_ID_CLIENTE, SchemDB.COL_CLIENTE_ID,
                SchemDB.TAB_PACK,
                SchemDB.COL_RESERVA_ID_PACK, SchemDB.COL_PACK_ID,
                SchemDB.TAB_SALA,
                SchemDB.COL_RESERVA_ID_SALA, SchemDB.COL_SALA_ID,
                SchemDB.TAB_SUPERVISOR,
                SchemDB.COL_RESERVA_ID_SUPERVISOR, SchemDB.COL_SUPERVISOR_ID,
                SchemDB.COL_RESERVA_ID_CLIENTE,
                SchemDB.COL_RESERVA_FECHA,
                SchemDB.COL_RESERVA_FECHA, SchemDB.COL_RESERVA_HORA
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, cliente.getIdCliente());
            preparedStatement.setDate(2, Date.valueOf(fecha));
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listaReservas.add(mapearReservaCompleta());
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return listaReservas;
    }

    public boolean cambiarEstadoReserva(String dni, LocalDate fecha, LocalTime hora, EstadoReserva estado) {
        if (dni == null || dni.isBlank() || fecha == null || hora == null || estado == null) {
            return false;
        }

        String query = String.format(
                "UPDATE %s r " +
                        "INNER JOIN %s c ON r.%s = c.%s " +
                        "SET r.%s = ?, r.%s = ? " +
                        "WHERE c.%s = ? AND r.%s = ? AND r.%s = ?",
                SchemDB.TAB_RESERVA,
                SchemDB.TAB_CLIENTE,
                SchemDB.COL_RESERVA_ID_CLIENTE,
                SchemDB.COL_CLIENTE_ID,
                SchemDB.COL_RESERVA_ESTADO,
                SchemDB.COL_RESERVA_ES_CONFIRMADO,
                SchemDB.COL_CLIENTE_DNI,
                SchemDB.COL_RESERVA_FECHA,
                SchemDB.COL_RESERVA_HORA
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, estado.name().toLowerCase());
            preparedStatement.setBoolean(2, estado == EstadoReserva.confirmada || estado == EstadoReserva.completada);
            preparedStatement.setString(3, dni.trim());
            preparedStatement.setDate(4, Date.valueOf(fecha));
            preparedStatement.setTime(5, Time.valueOf(hora));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }

        return false;
    }

    public boolean anularReserva(String dni, LocalDate fecha, LocalTime hora) {
        return cambiarEstadoReserva(dni, fecha, hora, EstadoReserva.cancelada);
    }

    public boolean completarReserva(String dni, LocalDate fecha, LocalTime hora) {
        return cambiarEstadoReserva(dni, fecha, hora, EstadoReserva.completada);
    }

    private Reserva mapearReservaCompleta() throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(resultSet.getInt(SchemDB.COL_RESERVA_ID));
        reserva.setFecha(resultSet.getDate(SchemDB.COL_RESERVA_FECHA).toLocalDate());
        reserva.setHora(resultSet.getTime(SchemDB.COL_RESERVA_HORA).toLocalTime());
        reserva.setEstado(EstadoReserva.valueOf(resultSet.getString(SchemDB.COL_RESERVA_ESTADO)));
        reserva.setEsConfirmado(resultSet.getBoolean(SchemDB.COL_RESERVA_ES_CONFIRMADO));

        Cliente cliente = new Cliente();
        cliente.setNombre(resultSet.getString("cliente_nombre"));
        cliente.setApellidos(resultSet.getString("cliente_apellidos"));
        cliente.setDni(resultSet.getString("cliente_dni"));
        reserva.setCliente(cliente);

        Pack pack = new Pack();
        pack.setIdPack(resultSet.getInt("pack_id"));
        pack.setNombre(resultSet.getString("pack_nombre"));
        pack.setDuracion(resultSet.getInt("pack_duracion"));
        reserva.setPack(pack);

        Sala sala = new Sala();
        sala.setIdSala(resultSet.getInt("sala_id"));
        sala.setNombre(resultSet.getString("sala_nombre"));
        reserva.setSala(sala);

        Supervisor supervisor = new Supervisor();
        supervisor.setIdSupervisor(resultSet.getInt("supervisor_id"));
        supervisor.setNombre(resultSet.getString("supervisor_nombre"));
        reserva.setSupervisor(supervisor);

        return reserva;
    }
}