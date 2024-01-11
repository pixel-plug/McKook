package com.meteor.mckook.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Database {

    void connect() throws SQLException;
    void disconnect() throws SQLException;
    void executeUpdate(String sql,Map<String,String> params,List<Object> parameterValue) throws SQLException;
    ResultSet executeQuery(String sql, Map<String,String> params) throws SQLException;
    ResultSet executeQuery(String sql, Map<String,String> params, List<Object> parameterValue) throws SQLException;
}
