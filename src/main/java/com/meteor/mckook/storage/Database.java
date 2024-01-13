package com.meteor.mckook.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Database {

    void connect() throws SQLException;
    void disconnect() throws SQLException;

    Connection getConnection();
}
