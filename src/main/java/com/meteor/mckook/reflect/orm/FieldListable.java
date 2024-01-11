package com.meteor.mckook.reflect.orm;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 数据库实体接口
 */
public interface FieldListable {

    /**
     * 获取类所有字段 (按照定义的列顺序)
     * @return
     */
    default List<Object> getFieldList() {
        Field[] fields = this.getClass().getDeclaredFields();
        Arrays.sort(fields, Comparator.comparingInt(f -> f.getAnnotation(FieldOrder.class).value()));

        List<Object> values = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                values.add(field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return values;
    }

    /**
     * 从 ResultSet 初始化字段
     * @param resultSet
     * @throws SQLException
     */
    default void populateFieldsFromResultSet(ResultSet resultSet) throws SQLException {
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                Object value = resultSet.getObject(fieldName);
                if (value != null) {
                    field.set(this, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing field: " + fieldName, e);
            } catch (SQLException e) {
                System.out.println("Column not found in ResultSet: " + fieldName);
            }
        }
    }
}
