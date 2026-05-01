package org.example.suenhator.dao;

import org.example.suenhator.database.DBConnection;
import org.example.suenhator.database.SchemDB;
import org.example.suenhator.model.Cliente;
import org.example.suenhator.model.Compra;
import org.example.suenhator.model.Pago;
import org.example.suenhator.model.enums.EstadoCompra;
import org.example.suenhator.model.enums.MetodoPago;
import org.example.suenhator.utils.AlertCreation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PagoDAO {

    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public PagoDAO() {
        connection = DBConnection.getConnection();
    }

    public Pago registrarPago(Compra compra, double importe, MetodoPago metodoPago, LocalDate fechaPago) {
        if (compra == null || compra.getIdCompra() <= 0) {
            AlertCreation.crearWarning("Pago inválido", "La compra seleccionada no es válida");
            return null;
        }

        if (existePagoParaCompra(compra)) {
            AlertCreation.crearWarning("Pago duplicado", "Ya existe un pago para esta compra");
            return null;
        }

        Pago pago = null;

        String query = String.format(
                "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                SchemDB.TAB_PAGO,
                SchemDB.COL_PAGO_FECHA_PAGO,
                SchemDB.COL_PAGO_IMPORTE,
                SchemDB.COL_PAGO_METODO,
                SchemDB.COL_PAGO_ID_COMPRA
        );

        try {
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, Date.valueOf(fechaPago));
            preparedStatement.setDouble(2, importe);
            preparedStatement.setString(3, metodoPago.name().toLowerCase());
            preparedStatement.setInt(4, compra.getIdCompra());

            int resultado = preparedStatement.executeUpdate();

            if (resultado > 0) {
                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    pago = new Pago();
                    pago.setIdPago(resultSet.getInt(1));
                    pago.setCompra(compra);
                    pago.setFechaPago(fechaPago);
                    pago.setImporte(importe);
                    pago.setMetodo(metodoPago);
                }
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return pago;
    }

    public ArrayList<Pago> obtenerPagos() {
        ArrayList<Pago> listaPagos = new ArrayList<>();

        String query = String.format("SELECT * FROM %s", SchemDB.TAB_PAGO);

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Pago pago = new Pago();
                pago.setIdPago(resultSet.getInt(SchemDB.COL_PAGO_ID));
                pago.setFechaPago(resultSet.getDate(SchemDB.COL_PAGO_FECHA_PAGO).toLocalDate());
                pago.setImporte(resultSet.getDouble(SchemDB.COL_PAGO_IMPORTE));
                pago.setMetodo(MetodoPago.valueOf(resultSet.getString(SchemDB.COL_PAGO_METODO)));
                listaPagos.add(pago);
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return listaPagos;
    }

    public ArrayList<Pago> obtenerPagosPorCompra(Compra compra) {
        ArrayList<Pago> listaPagos = new ArrayList<>();

        if (compra == null || compra.getIdCompra() == 0) {
            return listaPagos;
        }

        String query = String.format(
                "SELECT * FROM %s WHERE %s = ?",
                SchemDB.TAB_PAGO,
                SchemDB.COL_PAGO_ID_COMPRA
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, compra.getIdCompra());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Pago pago = new Pago();
                pago.setIdPago(resultSet.getInt(SchemDB.COL_PAGO_ID));
                pago.setCompra(compra);
                pago.setFechaPago(resultSet.getDate(SchemDB.COL_PAGO_FECHA_PAGO).toLocalDate());
                pago.setImporte(resultSet.getDouble(SchemDB.COL_PAGO_IMPORTE));
                pago.setMetodo(MetodoPago.valueOf(resultSet.getString(SchemDB.COL_PAGO_METODO)));
                listaPagos.add(pago);
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return listaPagos;
    }

    public boolean existePagoParaCompra(Compra compra) {
        if (compra == null || compra.getIdCompra() == 0) {
            return false;
        }

        String query = String.format(
                "SELECT 1 FROM %s WHERE %s = ? LIMIT 1",
                SchemDB.TAB_PAGO,
                SchemDB.COL_PAGO_ID_COMPRA
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, compra.getIdCompra());
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return false;
    }

    public ArrayList<Pago> obtenerPagosPorCliente(Cliente cliente) {
        ArrayList<Pago> listaPagos = new ArrayList<>();

        if (cliente == null || cliente.getIdCliente() == 0) {
            return listaPagos;
        }

        String query = String.format(
                "SELECT p.%s, p.%s, p.%s, p.%s, p.%s, " +  // columnas de pago
                        "c.%s, c.%s, c.%s " +              // columnas de compra
                        "FROM %s p " +
                        "JOIN %s c ON p.%s = c.%s " +
                        "WHERE c.%s = ? " +
                        "ORDER BY p.%s DESC, p.%s DESC",
                SchemDB.COL_PAGO_ID,
                SchemDB.COL_PAGO_FECHA_PAGO,
                SchemDB.COL_PAGO_IMPORTE,
                SchemDB.COL_PAGO_METODO,
                SchemDB.COL_PAGO_ID_COMPRA,
                SchemDB.COL_COMPRA_FECHA,
                SchemDB.COL_COMPRA_TOTAL,
                SchemDB.COL_COMPRA_ESTADO,
                SchemDB.TAB_PAGO,
                SchemDB.TAB_COMPRA,
                SchemDB.COL_PAGO_ID_COMPRA,
                SchemDB.COL_COMPRA_ID,
                SchemDB.COL_COMPRA_ID_CLIENTE,
                SchemDB.COL_PAGO_FECHA_PAGO,
                SchemDB.COL_PAGO_ID
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, cliente.getIdCliente());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(resultSet.getInt(SchemDB.COL_PAGO_ID_COMPRA));
                compra.setCliente(cliente);
                compra.setFecha(resultSet.getDate(SchemDB.COL_COMPRA_FECHA).toLocalDate());
                compra.setTotal(resultSet.getDouble(SchemDB.COL_COMPRA_TOTAL));
                compra.setEstado(EstadoCompra.valueOf(resultSet.getString(SchemDB.COL_COMPRA_ESTADO)));

                Pago pago = new Pago();
                pago.setIdPago(resultSet.getInt(SchemDB.COL_PAGO_ID));
                pago.setCompra(compra);
                pago.setFechaPago(resultSet.getDate(SchemDB.COL_PAGO_FECHA_PAGO).toLocalDate());
                pago.setImporte(resultSet.getDouble(SchemDB.COL_PAGO_IMPORTE));
                pago.setMetodo(MetodoPago.valueOf(resultSet.getString(SchemDB.COL_PAGO_METODO)));

                listaPagos.add(pago);
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return listaPagos;
    }
}