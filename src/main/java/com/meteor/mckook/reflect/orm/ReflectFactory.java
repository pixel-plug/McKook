package com.meteor.mckook.reflect.orm;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ReflectFactory {

    /**
     * 通过 ResultSet 填充对象
     * @param clazz
     * @param resultSet
     * @return
     * @param <T>
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T createAndPopulate(Class<T> clazz, ResultSet resultSet) throws
            SQLException, IllegalAccessException, InstantiationException {
        if (!clazz.isAnnotationPresent(ResultSetPopulatable.class)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not annotated with ResultSetPopulatable");
        }
        T instance = clazz.newInstance();
        if (instance instanceof FieldListable) {
            ((FieldListable) instance).populateFieldsFromResultSet(resultSet);
        }
        return instance;
    }


}
