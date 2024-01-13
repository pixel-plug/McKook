package com.meteor.mckook.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractDatabase implements Database {

    /**
     * 设置具体参数
     * @param preparedStatement
     * @param objects
     * @return
     */
    private PreparedStatement preparedStatementSetObject(PreparedStatement preparedStatement,List<Object> objects){
        if(objects==null) return preparedStatement;
        for (int i = 0; i < objects.size(); i++) {
            try {
                preparedStatement.setObject(i+1,objects.get(i));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return preparedStatement;
    }

    /**
     * 替换sql语句 {path}
     * @param sql
     * @param params
     * @return
     */
    private String putParams(String sql,Map<String,String> params){
        return params.entrySet().stream().reduce(sql, (r, entry) -> {
            return r.replace("{" + entry.getKey() + "}", entry.getValue());
        }, (r1, r2) -> r2);
    }


    /**
     * 执行更新sql语句
     * @param sql
     * @param params
     * @param parameterValue
     */
    public void executeUpdate(String sql, Map<String, String> params, List<Object> parameterValue){
        if(params!=null)
            sql = putParams(sql,params);

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = preparedStatementSetObject(getConnection().prepareStatement(sql),parameterValue);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 查询结果集
     * @param sql
     * @param params
     * @param parameterValue
     * @return
     */
    public <T> T executeQuery(String sql, Map<String,String> params, List<Object> parameterValue,
                                  Function<ResultSet,T> handler){
        if(params!=null) sql = putParams(sql,params);
        try(PreparedStatement preparedStatement = preparedStatementSetObject(getConnection().prepareStatement(sql),parameterValue);
            ResultSet resultSet = preparedStatement.executeQuery();)
        {
            return handler.apply(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
