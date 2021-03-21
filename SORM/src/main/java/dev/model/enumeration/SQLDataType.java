package dev.model.enumeration;

/**
 * Enumeration representing SQL equivalent java data types
 */
public enum SQLDataType {
    CHAR,
    TEXT,
    NUMERIC,
    BIT,
    TINYINT,
    SMALLINT,
    INTEGER,
    BIGINT,
    REAL,
    DOUBLE,
    DATE,
    TIME,
    TIMESTAMP,
    INVALID;

    /**
     * Returns the SQL equivalent data type of the provided value
     * @param value Value whose SQL equivalent is to be determined, must be Wrapper object for simple types
     * @param <T> Data type of value
     * @return SQL equivalent to data type T as an SQLDataType enumeration
     */
    public <T> SQLDataType convertToSQLDataType(T value) throws IllegalArgumentException{
        if(value == null)
            throw new IllegalArgumentException("Parameter value cannot be null");
        switch (value.getClass().getSimpleName()) {
            case "Character":
                return SQLDataType.CHAR;
            case "String":
                return SQLDataType.TEXT;
            case "Boolean":
                return SQLDataType.BIT;
            case "BigDecimal":
                return SQLDataType.NUMERIC;
            case "Byte":
                return SQLDataType.TINYINT;
            case "Short":
                return SQLDataType.SMALLINT;
            case "Integer":
                return SQLDataType.INTEGER;
            case "Long":
                return SQLDataType.BIGINT;
            case "Float":
                return SQLDataType.REAL;
            case "Double":
                return SQLDataType.DOUBLE;
            case "Date":
                return SQLDataType.DATE;
            case "Time":
                return SQLDataType.TIME;
            case "Timestamp":
                return SQLDataType.TIMESTAMP;
            default:
                return SQLDataType.INVALID;
        }
    }
}
