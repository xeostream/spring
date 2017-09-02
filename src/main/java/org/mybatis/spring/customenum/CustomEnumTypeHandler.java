package org.mybatis.spring.customenum;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomEnumTypeHandler<E extends CustomEnum> extends BaseTypeHandler<E> {

    private Class<E> type;

    private List<E> enums = new ArrayList<>();

    public CustomEnumTypeHandler(Class<E> type) {
        if (null == type) {
            throw new IllegalArgumentException("type argument cannot be null");
        }

        this.type = type;
        E[] customEnums = type.getEnumConstants();
        if (null != customEnums && customEnums.length > 0) {
            enums = Arrays.asList(customEnums);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, E e, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, e.getCode());
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String str = resultSet.getString(s);
        return resultSet.wasNull() ? null : findEnumByString(str);
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String str = resultSet.getString(i);
        return resultSet.wasNull() ? null : findEnumByString(str);
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String str = callableStatement.getString(i);
        return callableStatement.wasNull() ? null : findEnumByString(str);
    }

    private E findEnumByString(String str) {
        E e = null;

        for (E tmp : enums) {
            if (tmp.getCode().equals(str)) {
                e = tmp;
                break;
            }
        }

        if (null == e) {
            throw new RuntimeException("cannot convert " + str + " to " + type.getSimpleName() + " by code.");
        }
        return e;
    }
}
