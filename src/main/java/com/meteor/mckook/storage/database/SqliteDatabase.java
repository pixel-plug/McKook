package com.meteor.mckook.storage.database;

import com.meteor.mckook.McKook;
import com.meteor.mckook.storage.AbstractDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SqliteDatabase extends AbstractDatabase {

    private McKook plugin;

    private Connection connection;

    public SqliteDatabase(McKook plugin){
        this.plugin = plugin;
    }

    @Override
    public void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:"+plugin.getDataFolder().getPath()+"/database.db");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnect() throws SQLException {
        this.connection.close();
    }

    @Override
    public Connection getConnection() {
        return connection;
    }



}
