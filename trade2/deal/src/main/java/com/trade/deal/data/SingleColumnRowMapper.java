package com.trade.deal.data;

import com.trade.deal.util.JdbcUtils;
import com.trade.deal.util.NumberUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SingleColumnRowMapper<T> implements RowMapper<T> {

    private Class<T> requiredType;


    /**
     * Create a new SingleColumnRowMapper.
     * @see #setRequiredType
     */
    public SingleColumnRowMapper() {
    }

    /**
     * Create a new SingleColumnRowMapper.
     * @param requiredType the type that each result object is expected to match
     */
    public SingleColumnRowMapper(Class<T> requiredType) {
        this.requiredType = requiredType;
    }

    /**
     * Set the type that each result object is expected to match.
     * <p>If not specified, the column value will be exposed as
     * returned by the JDBC driver.
     */
    public void setRequiredType(Class<T> requiredType) {
        this.requiredType = requiredType;
    }


    /**
     * Extract a value for the single column in the current row.
     * <p>Validates that there is only one column selected,
     * then delegates to <code>getColumnValue()</code> and also
     * <code>convertValueToRequiredType</code>, if necessary.
     * @see ResultSetMetaData#getColumnCount()
     * @see #getColumnValue(ResultSet, int, Class)
     * @see #convertValueToRequiredType(Object, Class)
     */
    @SuppressWarnings("unchecked")
    public T mapRow(ResultSet rs) throws SQLException {
        // Validate column count.
        ResultSetMetaData rsmd = rs.getMetaData();
        int nrOfColumns = rsmd.getColumnCount();
        if (nrOfColumns != 1) {
        }

        // Extract column value from JDBC ResultSet.
        Object result = getColumnValue(rs, 1, this.requiredType);
        if (result != null && this.requiredType != null && !this.requiredType.isInstance(result)) {
            // Extracted value does not match already: try to convert it.
            try {
                return (T) convertValueToRequiredType(result, this.requiredType);
            }
            catch (IllegalArgumentException ex) {

            }
        }
        return (T) result;
    }

    protected Object getColumnValue(ResultSet rs, int index, Class requiredType) throws SQLException {
        if (requiredType != null) {
            return JdbcUtils.getResultSetValue(rs, index, requiredType);
        }
        else {
            // No required type specified -> perform default extraction.
            return getColumnValue(rs, index);
        }
    }

    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index);
    }

    /**
     * Convert the given column value to the specified required type.
     * Only called if the extracted column value does not match already.
     * <p>If the required type is String, the value will simply get stringified
     * via <code>toString()</code>. In case of a Number, the value will be
     * converted into a Number, either through number conversion or through
     * String parsing (depending on the value type).
     * @param value the column value as extracted from <code>getColumnValue()</code>
     * (never <code>null</code>)
     * @param requiredType the type that each result object is expected to match
     * (never <code>null</code>)
     * @return the converted value
     * @see #getColumnValue(ResultSet, int, Class)
     */
    @SuppressWarnings("unchecked")
    protected Object convertValueToRequiredType(Object value, Class requiredType) {
        if (String.class.equals(requiredType)) {
            return value.toString();
        }
        else if (Number.class.isAssignableFrom(requiredType)) {
            if (value instanceof Number) {
                // Convert original Number to target Number class.
                return NumberUtils.convertNumberToTargetClass(((Number) value), requiredType);
            }
            else {
                // Convert stringified value to target Number class.
                return NumberUtils.parseNumber(value.toString(), requiredType);
            }
        }
        else {
            throw new IllegalArgumentException(
                    "Value [" + value + "] is of type [" + value.getClass().getName() +
                            "] and cannot be converted to required type [" + requiredType.getName() + "]");
        }
    }

}
