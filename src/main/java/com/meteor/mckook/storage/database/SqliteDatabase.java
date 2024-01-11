package com.meteor.mckook.storage.database;

import com.meteor.mckook.McKook;
import com.meteor.mckook.storage.AbstractDatabase;
import com.meteor.mckook.storage.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SqliteDatabase extends AbstractDatabase {

    private McKook plugin;

    public SqliteDatabase(McKook plugin){

    }

    @Override
    public void connect() throws SQLException {

    }

    @Override
    public void disconnect() throws SQLException {

    }

    @Override
    public void executeUpdate(String sql, Map<String, String> params, List<Object> parameterValue) throws SQLException {

    }

    @Override
    public ResultSet executeQuery(String sql, Map<String, String> params) throws SQLException {
        return null;
    }

    @Override
    public ResultSet executeQuery(String sql, Map<String, String> params, List<Object> parameterValue) throws SQLException {
        return null;
    }


}
