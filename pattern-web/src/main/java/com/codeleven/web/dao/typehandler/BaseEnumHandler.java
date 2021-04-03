package com.codeleven.web.dao.typehandler;

import com.codeleven.common.constants.BaseEnum;
import com.codeleven.common.constants.ShoesSize;

import com.codeleven.common.constants.PatternSystemVendor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes({JdbcType.INTEGER, JdbcType.VARCHAR, JdbcType.CHAR})
@MappedTypes({ShoesSize.class, PatternSystemVendor.class})
public class BaseEnumHandler<E extends BaseEnum> extends BaseTypeHandler<E> {
    private Class<E> type;
    private E [] enums;

    public BaseEnumHandler(Class<E> type) {
        if (type == null)
            throw new IllegalArgumentException("Type argument cannot be null");
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null)
            throw new IllegalArgumentException(type.getSimpleName()
                    + " does not represent an enum type.");
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if(parameter.getValue() instanceof String){
            ps.setObject(i, parameter.getValue(), JdbcType.CHAR.TYPE_CODE);
        } else if(parameter.getValue() instanceof Integer) {
            ps.setObject(i, parameter.getValue(), JdbcType.INTEGER.TYPE_CODE);
        } else{
            throw new SQLException("未知的JdbcType");
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String i = rs.getString(columnName);
        if(rs.wasNull()){
            return null;
        }
        return locateEnumStatus(i);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String resultSetString = rs.getString(columnIndex);
        if(rs.wasNull()){
            return null;
        }
        return locateEnumStatus(resultSetString);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String resultSetString = cs.getString(columnIndex);
        if(cs.wasNull()){
            return null;
        }
        return locateEnumStatus(resultSetString);
    }

    /**
     * 枚举类型转换，由于构造函数获取了枚举的子类enums，让遍历更加高效快捷
     * @param value 数据库中存储的自定义value属性
     * @return value对应的枚举类
     */
    private E locateEnumStatus(String value) {
        for (E e : enums) {
            if(e.getValue() instanceof Integer){
                if (String.valueOf(e.getValue()).equals(value)) {
                    return e;
                }
            }else if(e.getValue() instanceof String){
                if(e.getValue().equals(value)){
                    return e;
                }
            }
        }
        throw new IllegalArgumentException("未知的枚举类型：" + value + ",请核对" + type.getSimpleName());
    }
}
