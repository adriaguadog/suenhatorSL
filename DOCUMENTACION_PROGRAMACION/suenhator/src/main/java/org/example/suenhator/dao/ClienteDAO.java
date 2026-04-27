package org.example.suenhator.dao;

import org.example.suenhator.database.DBConnection;
import org.example.suenhator.database.SchemDB;
import org.example.suenhator.model.Cliente;
import org.example.suenhator.utils.AlertCreation;

import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO {

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    //constructor con creacion de conexion si no existe


    public ClienteDAO() {
        connection=DBConnection.getConnection();
    }

    public int darDeAlta(Cliente cliente) {
            String query = String.format(
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    SchemDB.TAB_CLIENTE,
                    SchemDB.COL_CLIENTE_NOMBRE,
                    SchemDB.COL_CLIENTE_APELLIDOS,
                    SchemDB.COL_CLIENTE_DNI,
                    SchemDB.COL_CLIENTE_TELEFONO,
                    SchemDB.COL_CLIENTE_EMAIL,
                    SchemDB.COL_CLIENTE_FECHA_ALTA,
                    SchemDB.COL_CLIENTE_FECHA_NAC

                    );
            //creo el prepared statement
            try {
                preparedStatement = connection.prepareStatement(query);
                //parametrizar
                preparedStatement.setString(1, cliente.getNombre());
                preparedStatement.setString(2, cliente.getApellidos());
                preparedStatement.setString(3, cliente.getDni());
                preparedStatement.setString(4, cliente.getTelefono());
                preparedStatement.setString(5, cliente.getEmail());
                preparedStatement.setDate(6, java.sql.Date.valueOf(cliente.getFechaAlta()));
                preparedStatement.setDate(7, java.sql.Date.valueOf(cliente.getFechaNac()));

                return preparedStatement.executeUpdate();
                //numero de filas afectadas
            } catch (SQLException e) {
                AlertCreation.crearError("Error en la ejecucion", e.getMessage());
                return -1;
            }
        }

    public int modificarDatos(Cliente cliente) {
        if (cliente == null) {
            AlertCreation.crearWarning("Accion no autorizada", "El cliente es nulo");
            return -1;
        }

        String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
                SchemDB.TAB_CLIENTE,
                SchemDB.COL_CLIENTE_NOMBRE,
                SchemDB.COL_CLIENTE_APELLIDOS,
                SchemDB.COL_CLIENTE_DNI,
                SchemDB.COL_CLIENTE_TELEFONO,
                SchemDB.COL_CLIENTE_EMAIL,
                SchemDB.COL_CLIENTE_FECHA_ALTA,
                SchemDB.COL_CLIENTE_FECHA_NAC,
                SchemDB.COL_CLIENTE_ID
        );

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, cliente.getNombre());
            preparedStatement.setString(2, cliente.getApellidos());
            preparedStatement.setString(3, cliente.getDni());
            preparedStatement.setString(4, cliente.getTelefono());
            preparedStatement.setString(5, cliente.getEmail());
            preparedStatement.setDate(6, java.sql.Date.valueOf(cliente.getFechaAlta()));
            preparedStatement.setDate(7, java.sql.Date.valueOf(cliente.getFechaNac()));
            preparedStatement.setInt(8, cliente.getIdCliente());

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
            return -1;
        }
    }

    public int darDeBaja(Cliente cliente) {
        if (cliente == null) {
            return -1;
    }
        //busco el cliente con ese id
        //delete from clientes where id=cliente.getId
        String query= String.format("DELETE FROM %s WHERE %s=?;",
                SchemDB.TAB_CLIENTE,
                SchemDB.COL_CLIENTE_ID
        );
        try {
            preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1, cliente.getIdCliente());

            int resultado= preparedStatement.executeUpdate();
            return resultado;
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
            return -1;
        }
    }

    public Cliente buscarPorDni(String dni) {
        if (dni == null || dni.isBlank()) {
            return null;
        }
        String query = String.format(
                "SELECT * FROM %s WHERE %s = ?",
                SchemDB.TAB_CLIENTE,
                SchemDB.COL_CLIENTE_DNI
        );
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dni);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(resultSet.getInt(SchemDB.COL_CLIENTE_ID));
                cliente.setNombre(resultSet.getString(SchemDB.COL_CLIENTE_NOMBRE));
                cliente.setApellidos(resultSet.getString(SchemDB.COL_CLIENTE_APELLIDOS));
                cliente.setDni(resultSet.getString(SchemDB.COL_CLIENTE_DNI));
                cliente.setTelefono(resultSet.getString(SchemDB.COL_CLIENTE_TELEFONO));
                cliente.setEmail(resultSet.getString(SchemDB.COL_CLIENTE_EMAIL));
                cliente.setFechaAlta(resultSet.getDate(SchemDB.COL_CLIENTE_FECHA_ALTA).toLocalDate());
                cliente.setFechaNac(resultSet.getDate(SchemDB.COL_CLIENTE_FECHA_NAC).toLocalDate());
                return cliente;
            }
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }
        //si no se encuentra
        return null;
    }

    public ArrayList<Cliente> listarTodos() {
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        String query = String.format("SELECT * FROM %s",
                SchemDB.TAB_CLIENTE);
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(resultSet.getInt(SchemDB.COL_CLIENTE_ID));
                cliente.setNombre(resultSet.getString(SchemDB.COL_CLIENTE_NOMBRE));
                cliente.setApellidos(resultSet.getString(SchemDB.COL_CLIENTE_APELLIDOS));
                cliente.setDni(resultSet.getString(SchemDB.COL_CLIENTE_DNI));
                cliente.setTelefono(resultSet.getString(SchemDB.COL_CLIENTE_TELEFONO));
                cliente.setEmail(resultSet.getString(SchemDB.COL_CLIENTE_EMAIL));
                cliente.setFechaAlta(resultSet.getDate(SchemDB.COL_CLIENTE_FECHA_ALTA).toLocalDate());
                cliente.setFechaNac(resultSet.getDate(SchemDB.COL_CLIENTE_FECHA_NAC).toLocalDate());
                listaClientes.add(cliente);
            }
            return listaClientes;
        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecucion", e.getMessage());
        }
        return listaClientes; //si hay error la devolvera vacia
    }
}
