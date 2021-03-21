package dev.model.database;

import dev.model.enumeration.SQLDataType;
import java.util.Objects;

/**
 * Represents a single field in a table
 * <p>
 *     Stores value and its SQL equivalent data type.
 *     An IllegalArgumentException is thrown if value's
 *     data type could not be identified
 * </p>
 * @param <T> Data Type of value to be stored
 */
public class DataField<T>{
    private T value;
    private SQLDataType dataType;

    /**
     * Can only be created with a value who's data type is supported
     * <p>
     *     Supported Data Types: char, String, boolean, BigDecimal,
     *     byte, short, int, long, float, double, Date, Time, Timestamp
     * </p>
     * @param value value to be stored in this DataField.
     * @throws IllegalArgumentException Thrown when value parameter is null or data type of value is not supported
     */
    private DataField(T value) throws IllegalArgumentException{
        if(value == null)
            throw new IllegalArgumentException("Parameter value cannot be null");
        this.value = value;
        SQLDataType tmp = SQLDataType.INVALID.convertToSQLDataType(value);
        if(tmp == SQLDataType.INVALID)
            throw new IllegalArgumentException("Value could not be easily converted to SQL data type. " +
                    "\nAcceptable Types: char, String, boolean, BigDecimal, byte, short, int, long, float, double, Date, Time, Timestamp");
        this.dataType = tmp;
    }

    /**
     * Can only be created with a value who's data type is supported
     * <p>
     *     Supported Data Types: char, String, boolean, BigDecimal,
     *     byte, short, int, long, float, double, Date, Time, Timestamp
     * </p>
     * @param value value to be stored in this DataField.
     * @param <T> data type of value
     * @return returns a new DataField
     * @throws IllegalArgumentException Thrown when value parameter is null or data type of value is not supported
     */
    public static <T> DataField<T> createDataField(T value) throws IllegalArgumentException{
        return new DataField<>(value);
    }

    /**
     * @return Returns value stored in this DataField object
     */
    public T getValue() {
        return value;
    }

    /**
     * @return Returns SQLDataType representation of value's data type
     */
    public SQLDataType getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataField<?> dataField = (DataField<?>) o;
        return value.equals(dataField.value) && dataType == dataField.dataType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, dataType);
    }

    @Override
    public String toString() {
        return "DataField{" +
                "value=" + value +
                ", dataType=" + dataType +
                '}';
    }
}
