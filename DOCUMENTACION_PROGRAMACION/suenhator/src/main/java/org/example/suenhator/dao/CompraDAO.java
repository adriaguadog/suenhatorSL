package org.example.suenhator.dao;

import org.example.suenhator.database.DBConnection;
import org.example.suenhator.database.SchemDB;
import org.example.suenhator.model.Cliente;
import org.example.suenhator.model.Compra;
import org.example.suenhator.model.LineaCompra;
import org.example.suenhator.model.Pack;
import org.example.suenhator.model.enums.EstadoCompra;
import org.example.suenhator.utils.AlertCreation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public CompraDAO() {
        connection = DBConnection.getConnection();
    }

    public Compra registrarCompra(Cliente cliente) {
        if (cliente == null || cliente.getIdCliente() <= 0) {
            return null;
        }

        String query = String.format(
                "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                SchemDB.TAB_COMPRA,
                SchemDB.COL_COMPRA_FECHA,
                SchemDB.COL_COMPRA_TOTAL,
                SchemDB.COL_COMPRA_ESTADO,
                SchemDB.COL_COMPRA_ID_CLIENTE
        );

        try {
            ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setDouble(2, 0.0);
            ps.setString(3, EstadoCompra.pendiente.name());
            ps.setInt(4, cliente.getIdCliente());

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    Compra compra = new Compra();
                    compra.setIdCompra(rs.getInt(1));
                    compra.setCliente(cliente);
                    compra.setFecha(LocalDate.now());
                    compra.setTotal(0.0);
                    compra.setEstado(EstadoCompra.pendiente);
                    return compra;
                }
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return null;
    }

    // Inserta una línea asociada a una compra ya registrada
    public boolean registrarLineaCompra(Compra compra, LineaCompra linea) {
        if (compra == null || linea == null) {
            return false;
        }

        String query = String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
                SchemDB.TAB_LINEA_COMPRA,
                SchemDB.COL_LINEA_COMPRA_ID_COMPRA,
                SchemDB.COL_LINEA_COMPRA_ID_PACK,
                SchemDB.COL_LINEA_COMPRA_CANTIDAD,
                SchemDB.COL_LINEA_COMPRA_PRECIO_UNITARIO,
                SchemDB.COL_LINEA_COMPRA_SUBTOTAL
        );

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, compra.getIdCompra());
            ps.setInt(2, linea.getPack().getIdPack());
            ps.setInt(3, linea.getCantidad());
            ps.setDouble(4, linea.getPrecioUnitario());
            ps.setDouble(5, linea.getSubtotal());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return false;
    }

    // Actualiza el total de la compra tras insertar todas sus líneas
    public boolean actualizarTotalCompra(Compra compra, double total) {
        if (compra == null) {
            return false;
        }

        String query = String.format(
                "UPDATE %s SET %s = ? WHERE %s = ?",
                SchemDB.TAB_COMPRA,
                SchemDB.COL_COMPRA_TOTAL,
                SchemDB.COL_COMPRA_ID
        );

        try {
            ps = connection.prepareStatement(query);
            ps.setDouble(1, total);
            ps.setInt(2, compra.getIdCompra());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return false;
    }

    // Cambia el estado de una compra
    public boolean cambiarEstadoCompra(int idCompra, EstadoCompra nuevoEstado) {
        if (idCompra <= 0 || nuevoEstado == null) {
            return false;
        }

        String query = String.format(
                "UPDATE %s SET %s = ? WHERE %s = ?",
                SchemDB.TAB_COMPRA,
                SchemDB.COL_COMPRA_ESTADO,
                SchemDB.COL_COMPRA_ID
        );

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, idCompra);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return false;
    }

    // Obtiene todas las compras de un cliente
    public List<Compra> obtenerComprasPorCliente(Cliente cliente) {
        ArrayList<Compra> listaCompras = new ArrayList<>();

        if (cliente == null || cliente.getIdCliente() <= 0) {
            return listaCompras;
        }

        String query = String.format(
                // quiero todas las compras del cliente seleccionado
                "SELECT * FROM %s WHERE %s = ? ORDER BY %s DESC, %s DESC",
                SchemDB.TAB_COMPRA,
                SchemDB.COL_COMPRA_ID_CLIENTE,
                SchemDB.COL_COMPRA_FECHA,
                SchemDB.COL_COMPRA_ID
        );

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, cliente.getIdCliente());

            rs = ps.executeQuery();

            while (rs.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(rs.getInt(SchemDB.COL_COMPRA_ID));
                compra.setCliente(cliente);
                compra.setFecha(rs.getDate(SchemDB.COL_COMPRA_FECHA).toLocalDate());
                compra.setTotal(rs.getDouble(SchemDB.COL_COMPRA_TOTAL));
                compra.setEstado(EstadoCompra.valueOf(rs.getString(SchemDB.COL_COMPRA_ESTADO).toLowerCase()));
                listaCompras.add(compra);
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return listaCompras;
    }

    // Obtiene todas las líneas de una compra existente
    public List<LineaCompra> obtenerLineasPorCompra(Compra compra) {
        ArrayList<LineaCompra> listaLineas = new ArrayList<>();

        if (compra == null || compra.getIdCompra() <= 0) {
            return listaLineas;
        }

        String query = String.format(
                "SELECT * FROM %s WHERE %s = ?",
                SchemDB.TAB_LINEA_COMPRA,
                SchemDB.COL_LINEA_COMPRA_ID_COMPRA
        );

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, compra.getIdCompra());

            rs = ps.executeQuery();

            while (rs.next()) {
                // Reconstruimos el Pack solo con id y precio
                Pack pack = new Pack();
                pack.setIdPack(rs.getInt(SchemDB.COL_LINEA_COMPRA_ID_PACK));

                double precioUnitario = rs.getDouble(SchemDB.COL_LINEA_COMPRA_PRECIO_UNITARIO);
                int cantidad = rs.getInt(SchemDB.COL_LINEA_COMPRA_CANTIDAD);
                double subtotal = rs.getDouble(SchemDB.COL_LINEA_COMPRA_SUBTOTAL);

                LineaCompra linea = new LineaCompra(
                        compra,
                        pack,
                        cantidad,
                        precioUnitario,
                        subtotal
                );

                listaLineas.add(linea);
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return listaLineas;
    }

    // Borra todas las líneas de una compra
    public boolean borrarLineasPorCompra(Compra compra) {
        if (compra == null || compra.getIdCompra() <= 0) {
            return false;
        }

        String query = String.format(
                "DELETE FROM %s WHERE %s = ?",
                SchemDB.TAB_LINEA_COMPRA,
                SchemDB.COL_LINEA_COMPRA_ID_COMPRA
        );

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, compra.getIdCompra());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return false;
    }

    // Actualiza una compra existente sobrescribiendo sus líneas y su total
    public boolean actualizarCompraConLineas(Compra compra, List<LineaCompra> lineas) {
        if (compra == null || compra.getIdCompra() <= 0 || lineas == null) {
            return false;
        }

        // Borramos las líneas actuales
        if (!borrarLineasPorCompra(compra)) {
            return false;
        }

        // Insertamos las nuevas líneas y calculamos el total
        double total = 0.0;

        for (LineaCompra linea : lineas) {
            boolean lineaInsertada = registrarLineaCompra(compra, linea);

            if (!lineaInsertada) {
                return false;
            }

            total += linea.getSubtotal();
        }

        // Actualizamos el total en la cabecera
        return actualizarTotalCompra(compra, total);
    }


}