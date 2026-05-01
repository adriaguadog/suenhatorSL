package org.example.suenhator.dao;

import org.example.suenhator.database.DBConnection;
import org.example.suenhator.database.SchemDB;
import org.example.suenhator.model.Pack;
import org.example.suenhator.utils.AlertCreation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PackDAO {

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    public PackDAO() {
        connection = DBConnection.getConnection();
    }

    public ArrayList<Pack> obtenerPacks() {
        ArrayList<Pack> listaPacks = new ArrayList<>();

        String query = String.format("SELECT * FROM %s", SchemDB.TAB_PACK);

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Pack pack = new Pack(
                        resultSet.getString(SchemDB.COL_PACK_NOMBRE),
                        resultSet.getString(SchemDB.COL_PACK_DESCRIPCION),
                        resultSet.getString(SchemDB.COL_PACK_TIPO_PACK),
                        resultSet.getInt(SchemDB.COL_PACK_DURACION),
                        resultSet.getDouble(SchemDB.COL_PACK_PRECIO),
                        resultSet.getBoolean(SchemDB.COL_PACK_ES_PREMIUM),
                        resultSet.getInt(SchemDB.COL_PACK_AFORO),
                        resultSet.getBoolean(SchemDB.COL_PACK_ES_18)
                );

                pack.setIdPack(resultSet.getInt(SchemDB.COL_PACK_ID));

                listaPacks.add(pack);
            }

        } catch (SQLException e) {
            AlertCreation.crearError("Error en la ejecución", e.getMessage());
        }

        return listaPacks;
    }
}