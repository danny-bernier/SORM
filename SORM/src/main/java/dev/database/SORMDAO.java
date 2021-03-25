package dev.database;

import dev.model.annotation.SORMNoArgConstructor;
import dev.model.database.DataField;
import dev.model.database.DataReference;
import dev.model.exception.SORMAccessException;
import dev.model.exception.SORMObjectRetrievalException;
import dev.utility.reflection.POJOPropertyGetSet;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Optional;

/**
 * Generic DAO for use with any object utilizing SORM annotations
 * @param <T> The type of object on which CRUD operations will be performed
 * @param <I> The type of the id (primary key) of object <T>
 */
public class SORMDAO<T, I> implements DAO<T, I>{

    private final Connection CONNECTION;
    private final Class<T> OBJECT_T_CLASS;
    private final Class<I> ID_I_CLASS;

    /**
     * Constructor gathers connection, and stores the class of T and I generics
     * @param typeOfT Class of generic type T
     * @param typeOfI Class of generic type I
     * @throws SQLException Thrown when an error is encountered creating the connection
     */
    public SORMDAO(Class<T> typeOfT, Class<I> typeOfI) throws SQLException{
        this.CONNECTION = DBConnection.getInstance().getConnection();
        this.OBJECT_T_CLASS = typeOfT;
        this.ID_I_CLASS = typeOfI;
    }

    /**
     * Retrieves an object from the database using that object's ID (primary key) as denoted with {@link dev.model.annotation.SORMID}
     * @param id id of the object being retrieved from the database
     * @return Returns Optional which either contains the object if it exists in the database, or an empty optional if it does not
     * @throws SQLException Thrown when unable to successfully query database
     * @throws SORMAccessException Thrown when one or more properties of an object (properties marked with {@link dev.model.annotation.SORMID}, {@link dev.model.annotation.SORMField}, or {@link dev.model.annotation.SORMReference}) are inaccessible
     * @throws IllegalArgumentException Thrown when parameter id is null
     * @throws SORMObjectRetrievalException Thrown when a fatal issue is encountered when retrieving and recreating an object from the database
     */
    @Override
    public Optional<T> getById(I id) throws SQLException, SORMAccessException, IllegalArgumentException, SORMObjectRetrievalException {

        if(id == null)
            throw new IllegalArgumentException("Parameter id cannot be null");

        //return empty if there isn't even a table for the object's class
        if(!objectTableExists(OBJECT_T_CLASS)) {
            //todo remove breadcrumb
//            System.out.println("no table");
            return Optional.empty();
        }

        String idFieldName = POJOPropertyGetSet.getIDNameByClass(OBJECT_T_CLASS);
        DataField<Object> idDataField = DataField.createDataField(id, idFieldName);
        //todo remove bread
//        System.out.println("Id value to select in " + OBJECT_T_CLASS.getSimpleName());
//        System.out.println(id);

        try (PreparedStatement statement = CONNECTION.prepareStatement("select * from " + OBJECT_T_CLASS.getSimpleName() + " where " + idFieldName + " = ?")) {
            switch (idDataField.getDataType()) {
                case CHAR:
                    statement.setString(1, String.valueOf(idDataField.getValue()));
                    break;
                case TEXT:
                    statement.setString(1, (String) idDataField.getValue());
                    break;
                case BIT:
                    statement.setBoolean(1, (boolean) idDataField.getValue());
                    break;
                case NUMERIC:
                    statement.setBigDecimal(1, (BigDecimal) idDataField.getValue());
                    break;
                case TINYINT:
                    statement.setByte(1, (byte) idDataField.getValue());
                    break;
                case SMALLINT:
                    statement.setShort(1, (short) idDataField.getValue());
                    break;
                case INTEGER:
                    statement.setInt(1, (int) idDataField.getValue());
                    break;
                case BIGINT:
                    statement.setLong(1, (long) idDataField.getValue());
                    break;
                case REAL:
                    statement.setFloat(1, (float) idDataField.getValue());
                    break;
                case DOUBLE:
                    statement.setDouble(1, (double) idDataField.getValue());
                    break;
                case DATE:
                    statement.setDate(1, (Date) idDataField.getValue());
                    break;
                case TIME:
                    statement.setTime(1, (Time) idDataField.getValue());
                    break;
                case TIMESTAMP:
                    statement.setTimestamp(1, (Timestamp) idDataField.getValue());
                    break;
                case INVALID:
                    throw new IllegalArgumentException("Data type is INVALID, and not supported");
            }

            //getting result set
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            //todo remove breadcrum
//            System.out.println(resultSet.getMetaData().getColumnCount());
//            System.out.println(OBJECT_T_CLASS.getSimpleName());
//            System.out.println(idFieldName);
//            System.out.println(idDataField.getValue());

            //Creating Object
            Constructor[] constructors = OBJECT_T_CLASS.getDeclaredConstructors();
            Constructor noArg;
            for (Constructor c:constructors) {
                if (c.isAnnotationPresent(SORMNoArgConstructor.class)) {

                    //todo remove breadcrumb
                    //System.out.println("found constructor with annotation");

                    c.setAccessible(true);
                    T object = OBJECT_T_CLASS.cast(c.newInstance());

                    idDataField = POJOPropertyGetSet.getID(object);
                    List<DataField<Object>> fields = POJOPropertyGetSet.getFields(object);
                    List<DataReference<Object>> references = POJOPropertyGetSet.getReference(object);

                    Field idField = OBJECT_T_CLASS.getDeclaredField(idDataField.getValueFieldName());
                    idField.setAccessible(true);
                    switch (idDataField.getDataType()) {
                        case CHAR:
                            idField.setChar(object, resultSet.getString(1).charAt(0));
                            break;
                        case TEXT:
                            idField.set(object, resultSet.getString(1));
                            break;
                        case BIT:
                            idField.setBoolean(object, resultSet.getBoolean(1));
                            break;
                        case NUMERIC:
                            idField.set(object, resultSet.getBigDecimal(1));
                            break;
                        case TINYINT:
                            idField.setByte(object, Byte.valueOf(resultSet.getByte(1)));
                            break;
                        case SMALLINT:
                            idField.setShort(object, resultSet.getShort(1));
                            break;
                        case INTEGER:
                            idField.setInt(object, resultSet.getInt(1));
                            break;
                        case BIGINT:
                            idField.setLong(object, resultSet.getLong(1));
                            break;
                        case REAL:
                            idField.setFloat(object, resultSet.getFloat(1));
                        case DOUBLE:
                            idField.setDouble(object, resultSet.getDouble(1));
                            break;
                        case DATE:
                            idField.set(object, resultSet.getDate(1));
                            break;
                        case TIME:
                            idField.set(object, resultSet.getTime(1));
                            break;
                        case TIMESTAMP:
                            idField.set(object, resultSet.getTimestamp(1));
                            break;
                        case INVALID:
                            throw new IllegalArgumentException("Data type is INVALID, and not supported");
                    }

                    for(int i = 0; i < fields.size(); i++){
                        Field f = OBJECT_T_CLASS.getDeclaredField(fields.get(i).getValueFieldName());
                        f.setAccessible(true);
                        switch (fields.get(i).getDataType()) {
                            case CHAR:
                                f.setChar(object, resultSet.getString(2+i).charAt(0));
                                break;
                            case TEXT:
                                f.set(object, resultSet.getString(2+i));
                                break;
                            case BIT:
                                f.setBoolean(object, resultSet.getBoolean(2+i));
                                break;
                            case NUMERIC:
                                f.set(object, resultSet.getBigDecimal(2+i));
                                break;
                            case TINYINT:
                                f.setByte(object, resultSet.getByte(2+i));
                                break;
                            case SMALLINT:
                                f.setShort(object, resultSet.getShort(2+i));
                                break;
                            case INTEGER:
                                f.setInt(object, resultSet.getInt(2+i));
                                break;
                            case BIGINT:
                                f.setLong(object, resultSet.getLong(2+i));
                                break;
                            case REAL:
                                f.setFloat(object, resultSet.getFloat(2+i));
                                break;
                            case DOUBLE:
                                f.setDouble(object, resultSet.getDouble(2+i));
                                break;
                            case DATE:
                                f.set(object, resultSet.getDate(2+i));
                                break;
                            case TIME:
                                f.set(object, resultSet.getTime(2+i));
                                break;
                            case TIMESTAMP:
                                f.set(object, resultSet.getTimestamp(2+i));
                                break;
                            case INVALID:
                                throw new IllegalArgumentException("Data type is INVALID, and not supported");
                        }
                    }

                    for(int i = 0; i < references.size(); i++){
                        Class ftClass = references.get(i).getREFERENCE().getClass();
                        Class fiClass = references.get(i).getREFERENCES_ID().getValue().getClass();
                        Field f = OBJECT_T_CLASS.getDeclaredField(references.get(i).getREFERENCE_NAME());
                        f.setAccessible(true);

                        //todo remove breadcrumb
//                        System.out.println("HERE");
//                        System.out.println(references.get(i).getREFERENCES_ID().getValue());

                        switch (references.get(i).getREFERENCES_ID().getDataType()) {
                            case CHAR:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getString(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case TEXT:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getString(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case BIT:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getBoolean(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case NUMERIC:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getBigDecimal(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case TINYINT:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getByte(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case SMALLINT:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getShort(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case INTEGER:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getInt(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case BIGINT:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getLong(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case REAL:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getFloat(references.get(i).getREFERENCE_NAME())).get());
                            case DOUBLE:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getDouble(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case DATE:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getDate(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case TIME:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getTime(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case TIMESTAMP:
                                f.set(object, new SORMDAO<>(ftClass, fiClass).getById(resultSet.getTimestamp(references.get(i).getREFERENCE_NAME())).get());
                                break;
                            case INVALID:
                                throw new IllegalArgumentException("Data type is INVALID, and not supported");
                        }
                    }
                    return Optional.of(OBJECT_T_CLASS.cast(object));
                }
            }

        } catch (Exception e){
            //todo remove stacktrace
            //e.printStackTrace();
        }
        return Optional.empty();
    }


    /**
     * Creates an object representation in the database
     * @param object The object being added to the database
     * @return Returns true if the object was added successfully, false if not
     * @throws SORMAccessException Thrown when an object's fields, ID, or references could not be accessed
     * @throws IllegalArgumentException Thrown when object parameter is null
     */
    @Override
    public boolean create(T object) throws SORMAccessException, IllegalArgumentException {

        if(object == null)
            throw new IllegalArgumentException("Parameter object cannot be null");

        if(createTable(object)) {
            return addObject(object);
        }

        return false;
    }

    /**
     * Updates the object in the database with matching {@link dev.model.annotation.SORMID} with provided object
     * <p>
     *     Also recursively updates references marked by {@link dev.model.annotation.SORMReference} to ensure accurate data
     * </p>
     * @param object The object being updated in the database
     * @return Returns true if object was updated successfully, returns false if not
     * @throws SORMAccessException Thrown when one or more properties of an object (properties marked with {@link dev.model.annotation.SORMID}, {@link dev.model.annotation.SORMField}, or {@link dev.model.annotation.SORMReference}) are inaccessible
     * @throws IllegalArgumentException Thrown when object parameter is null
     */
    @Override
    public boolean update(T object) throws SORMAccessException, IllegalArgumentException {
        if(object == null)
            throw new IllegalArgumentException("Parameter object cannot be null");
        return updateHelper(object);
    }

    /**
     * Recursive helper method for public update()
     * <p>
     *     Updates the object in the database with matching {@link dev.model.annotation.SORMID} with provided object.
     *     Also recursively updates references marked by {@link dev.model.annotation.SORMReference} to ensure accurate data
     * </p>
     * @param object The object being updated in the database
     * @return Returns true if object was updated successfully, returns false if not
     * @throws SORMAccessException Thrown when one or more properties of an object (properties marked with {@link dev.model.annotation.SORMID}, {@link dev.model.annotation.SORMField}, or {@link dev.model.annotation.SORMReference}) are inaccessible
     * @throws IllegalArgumentException Thrown when object parameter is null
     */
    private <C> boolean updateHelper(C object) throws SORMAccessException, IllegalArgumentException {

        if(object == null)
            throw new IllegalArgumentException("Parameter object cannot be null");
        if(!objectTableExists(object.getClass()))
            return false;

        //Gathering information needed to add object
        DataField<Object> id = POJOPropertyGetSet.getID(object);
        List<DataField<Object>> dataFields = POJOPropertyGetSet.getFields(object);
        List<DataReference<Object>> references = POJOPropertyGetSet.getReference(object);

        //counts of fields and references for use later in constructing statement
        int numberOfFields = 0;
        int numberOfReferences = 0;

        //recursive call to add reference / foreign keys first to reference them later on
        for (DataReference<Object> ref:references) {
            if(!updateHelper(ref.getREFERENCE()))
                return false;
        }

        //constructing SQL statement
        StringBuilder stringBuilder = new StringBuilder();

        //defining name of table as name of object's class
        stringBuilder.append("update " + object.getClass().getSimpleName() + " set ");

        //adding primary key as first entry
        stringBuilder.append(id.getValueFieldName() + " = ?, ");

        //adding data fields if any exist
        for (DataField<Object> field : dataFields) {
            stringBuilder.append(field.getValueFieldName() + " = ?, ");
            numberOfFields++;
        }

        //adding reference / foreign key fields if any exist
        for (DataReference<Object> ref:references) {
            stringBuilder.append(ref.getREFERENCE_NAME() + " = ?, ");
            numberOfReferences++;
        }

        //closing out sql statement with serializable field and conditional matching ID
        stringBuilder.setLength(stringBuilder.length()-2);
        stringBuilder.append(" where ");
        stringBuilder.append(id.getValueFieldName() + " = ?");

        //executing sql statement to create table
        try(PreparedStatement statement = CONNECTION.prepareStatement(stringBuilder.toString())) {
            //setting id (primary key)
            //todo find a more elegent solution
            switch (id.getDataType()) {
                case CHAR:
                    statement.setString(1, (String) id.getValue());
                    break;
                case TEXT:
                    statement.setString(1, (String) id.getValue());
                    break;
                case BIT:
                    statement.setBoolean(1, (boolean) id.getValue());
                    break;
                case NUMERIC:
                    statement.setBigDecimal(1, (BigDecimal) id.getValue());
                    break;
                case TINYINT:
                    statement.setByte(1, (byte) id.getValue());
                    break;
                case SMALLINT:
                    statement.setShort(1, (short) id.getValue());
                    break;
                case INTEGER:
                    statement.setInt(1, (int) id.getValue());
                    break;
                case BIGINT:
                    statement.setLong(1, (long) id.getValue());
                    break;
                case REAL:
                    statement.setFloat(1, (float) id.getValue());
                    break;
                case DOUBLE:
                    statement.setDouble(1, (double) id.getValue());
                    break;
                case DATE:
                    statement.setDate(1, (Date) id.getValue());
                    break;
                case TIME:
                    statement.setTime(1, (Time) id.getValue());
                    break;
                case TIMESTAMP:
                    statement.setTimestamp(1, (Timestamp) id.getValue());
                    break;
                case INVALID:
                    throw new IllegalArgumentException("Data type is INVALID, and not supported");
            }

            //setting fields
            for(int i = 0; i < numberOfFields; i++){
                //todo find a better solution
                switch (dataFields.get(i).getDataType()) {
                    case CHAR:
                        statement.setString(i + 2, (String) dataFields.get(i).getValue());
                        break;
                    case TEXT:
                        statement.setString(i + 2, (String) dataFields.get(i).getValue());
                        break;
                    case BIT:
                        statement.setBoolean(i + 2, (boolean) dataFields.get(i).getValue());
                        break;
                    case NUMERIC:
                        statement.setBigDecimal(i + 2, (BigDecimal) dataFields.get(i).getValue());
                        break;
                    case TINYINT:
                        statement.setByte(i + 2, (byte) dataFields.get(i).getValue());
                        break;
                    case SMALLINT:
                        statement.setShort(i + 2, (short) dataFields.get(i).getValue());
                        break;
                    case INTEGER:
                        statement.setInt(i + 2, (int) dataFields.get(i).getValue());
                        break;
                    case BIGINT:
                        statement.setLong(i + 2, (long) dataFields.get(i).getValue());
                        break;
                    case REAL:
                        statement.setFloat(i + 2, (float) dataFields.get(i).getValue());
                        break;
                    case DOUBLE:
                        statement.setDouble(i + 2, (double) dataFields.get(i).getValue());
                        break;
                    case DATE:
                        statement.setDate(i + 2, (Date) dataFields.get(i).getValue());
                        break;
                    case TIME:
                        statement.setTime(i + 2, (Time) dataFields.get(i).getValue());
                        break;
                    case TIMESTAMP:
                        statement.setTimestamp(i + 2, (Timestamp) dataFields.get(i).getValue());
                        break;
                    case INVALID:
                        throw new IllegalArgumentException("Data type is INVALID, and not supported");
                }
            }

            //setting references
            for(int i = 0; i < numberOfReferences; i++){
                //todo find a more elegent solution
                switch (references.get(i).getREFERENCES_ID().getDataType()) {
                    case CHAR:
                        statement.setString(i+2+numberOfFields, (String) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case TEXT:
                        statement.setString(i+2+numberOfFields, (String) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case BIT:
                        statement.setBoolean(i+2+numberOfFields, (boolean) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case NUMERIC:
                        statement.setBigDecimal(i+2+numberOfFields, (BigDecimal) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case TINYINT:
                        statement.setByte(i+2+numberOfFields, (byte) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case SMALLINT:
                        statement.setShort(i+2+numberOfFields, (short) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case INTEGER:
                        statement.setInt(i+2+numberOfFields, (int) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case BIGINT:
                        statement.setLong(i+2+numberOfFields, (long) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case REAL:
                        statement.setFloat(i+2+numberOfFields, (float) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case DOUBLE:
                        statement.setDouble(i+2+numberOfFields, (double) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case DATE:
                        statement.setDate(i+2+numberOfFields, (Date) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case TIME:
                        statement.setTime(i+2+numberOfFields, (Time) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case TIMESTAMP:
                        statement.setTimestamp(i+2+numberOfFields, (Timestamp) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case INVALID:
                        throw new IllegalArgumentException("Data type is INVALID, and not supported");
                }
            }
            //setting object
            //statement.setObject(2+numberOfFields+numberOfReferences, object);

            //setting id for where conditional
            switch (id.getDataType()) {
                case CHAR:
                    statement.setString(2+numberOfFields+numberOfReferences, (String) id.getValue());
                    break;
                case TEXT:
                    statement.setString(2+numberOfFields+numberOfReferences, (String) id.getValue());
                    break;
                case BIT:
                    statement.setBoolean(2+numberOfFields+numberOfReferences, (boolean) id.getValue());
                    break;
                case NUMERIC:
                    statement.setBigDecimal(2+numberOfFields+numberOfReferences, (BigDecimal) id.getValue());
                    break;
                case TINYINT:
                    statement.setByte(2+numberOfFields+numberOfReferences, (byte) id.getValue());
                    break;
                case SMALLINT:
                    statement.setShort(2+numberOfFields+numberOfReferences, (short) id.getValue());
                    break;
                case INTEGER:
                    statement.setInt(2+numberOfFields+numberOfReferences, (int) id.getValue());
                    break;
                case BIGINT:
                    statement.setLong(2+numberOfFields+numberOfReferences, (long) id.getValue());
                    break;
                case REAL:
                    statement.setFloat(2+numberOfFields+numberOfReferences, (float) id.getValue());
                    break;
                case DOUBLE:
                    statement.setDouble(2+numberOfFields+numberOfReferences, (double) id.getValue());
                    break;
                case DATE:
                    statement.setDate(2+numberOfFields+numberOfReferences, (Date) id.getValue());
                    break;
                case TIME:
                    statement.setTime(2+numberOfFields+numberOfReferences, (Time) id.getValue());
                    break;
                case TIMESTAMP:
                    statement.setTimestamp(2+numberOfFields+numberOfReferences, (Timestamp) id.getValue());
                    break;
                case INVALID:
                    throw new IllegalArgumentException("Data type is INVALID, and not supported");
            }
            //returning execute
            return statement.executeUpdate() > 0;
        } catch (SQLException e){
            //todo remove stack trace
            //e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param object The object being deleted from the database
     * @throws SORMAccessException
     */
    @Override
    public void delete(T object) throws SORMAccessException {
        if(object == null)
            return;
        deleteHelper(object);
    }

    /**
     * Helper method for recursive delete
     * @param object
     * @param <C>
     * @throws SORMAccessException
     */
    private <C> void deleteHelper(C object) throws SORMAccessException {
        if(object == null)
            return;

        if(!objectTableExists(object.getClass()))
            return;

        //Gathering information needed to add object
        DataField<Object> id = POJOPropertyGetSet.getID(object);
        List<DataField<Object>> dataFields = POJOPropertyGetSet.getFields(object);
        List<DataReference<Object>> references = POJOPropertyGetSet.getReference(object);

        //counts of fields and references for use later in constructing statement
        int numberOfFields = 0;
        int numberOfReferences = 0;

        //recursive call to add reference / foreign keys first to reference them later on
        for (DataReference<Object> ref:references) {
            deleteHelper(ref.getREFERENCE());
        }

        //creating delete statement
        String sqlQuery = "delete from " + object.getClass().getSimpleName() + " where " + id.getValueFieldName() + " = ?";

        //executing sql statement to create table
        try(PreparedStatement statement = CONNECTION.prepareStatement(sqlQuery)) {
            //setting id (primary key)
            //todo find a more elegent solution
            switch (id.getDataType()) {
                case CHAR:
                    statement.setString(1, (String) id.getValue());
                    break;
                case TEXT:
                    statement.setString(1, (String) id.getValue());
                    break;
                case BIT:
                    statement.setBoolean(1, (boolean) id.getValue());
                    break;
                case NUMERIC:
                    statement.setBigDecimal(1, (BigDecimal) id.getValue());
                    break;
                case TINYINT:
                    statement.setByte(1, (byte) id.getValue());
                    break;
                case SMALLINT:
                    statement.setShort(1, (short) id.getValue());
                    break;
                case INTEGER:
                    statement.setInt(1, (int) id.getValue());
                    break;
                case BIGINT:
                    statement.setLong(1, (long) id.getValue());
                    break;
                case REAL:
                    statement.setFloat(1, (float) id.getValue());
                    break;
                case DOUBLE:
                    statement.setDouble(1, (double) id.getValue());
                    break;
                case DATE:
                    statement.setDate(1, (Date) id.getValue());
                    break;
                case TIME:
                    statement.setTime(1, (Time) id.getValue());
                    break;
                case TIMESTAMP:
                    statement.setTimestamp(1, (Timestamp) id.getValue());
                    break;
                case INVALID:
                    throw new IllegalArgumentException("Data type is INVALID, and not supported");
            }
            //executing sql statement
            statement.executeUpdate();
        } catch (SQLException e){e.printStackTrace();}
    }

    /**
     * Queries database to see if table exists.
     * @param clazz The entity's class who's table is being checked.
     * @return Returns true if table with name = name of object exists, returns false if no table exists.
     */
    private boolean objectTableExists(Class<?> clazz){
        try(PreparedStatement statement = CONNECTION.prepareStatement("select 1 from " + clazz.getSimpleName())) {
            statement.executeQuery();
            return true;
        } catch (SQLException ignored){
            return false;
        }
    }

    /**
     * Adds object to an existing table
     * @param object The object to be added
     * @param <C> The data type of the object being added
     * @return Returns true if object added successfully, false if unsuccessful
     * @throws SORMAccessException Thrown if unable to access object's fields and references
     */
    private <C> boolean addObject(C object) throws SORMAccessException{
        //Gathering information needed to add object
        DataField<Object> id = POJOPropertyGetSet.getID(object);
        List<DataField<Object>> dataFields = POJOPropertyGetSet.getFields(object);
        List<DataReference<Object>> references = POJOPropertyGetSet.getReference(object);

        //counts of fields and references for use later in constructing statement
        int numberOfFields = 0;
        int numberOfReferences = 0;

        //todo remove bread
        //System.out.println(references);

        //recursive call to add reference / foreign keys first to reference them later on
        for (DataReference<Object> ref:references) {
            if(!addObject(ref.getREFERENCE()))
                return false;
        }

        //constructing SQL statement
        StringBuilder stringBuilder = new StringBuilder();

        //defining name of table as name of object's class
        stringBuilder.append("insert into " + object.getClass().getSimpleName() + " values (");

        //adding primary key as first entry
        stringBuilder.append("?, ");

        //adding data fields if any exist
        for (DataField<Object> field : dataFields) {
            stringBuilder.append("?, ");
            numberOfFields++;
        }

        //adding reference / foreign key fields if any exist
        for (DataReference<Object> ref:references) {
            stringBuilder.append("?, ");
            numberOfReferences++;
        }

        //closing out sql statement
        stringBuilder.setLength(stringBuilder.length()-2);
        stringBuilder.append(")");


        //executing sql statement to create table
        try(PreparedStatement statement = CONNECTION.prepareStatement(stringBuilder.toString())) {
            //setting id (primary key)
            //todo find a more elegent solution
            switch (id.getDataType()) {
                case CHAR:
                    statement.setString(1, String.valueOf(id.getValue()));
                    break;
                case TEXT:
                    statement.setString(1, (String) id.getValue());
                    break;
                case BIT:
                    statement.setBoolean(1, (boolean) id.getValue());
                    break;
                case NUMERIC:
                    statement.setBigDecimal(1, (BigDecimal) id.getValue());
                    break;
                case TINYINT:
                    statement.setByte(1, (byte) id.getValue());
                    break;
                case SMALLINT:
                    statement.setShort(1, (short) id.getValue());
                    break;
                case INTEGER:
                    statement.setInt(1, (int) id.getValue());
                    break;
                case BIGINT:
                    statement.setLong(1, (long) id.getValue());
                    break;
                case REAL:
                    statement.setFloat(1, (float) id.getValue());
                    break;
                case DOUBLE:
                    statement.setDouble(1, (double) id.getValue());
                    break;
                case DATE:
                    statement.setDate(1, (Date) id.getValue());
                    break;
                case TIME:
                    statement.setTime(1, (Time) id.getValue());
                    break;
                case TIMESTAMP:
                    statement.setTimestamp(1, (Timestamp) id.getValue());
                    break;
                case INVALID:
                    throw new IllegalArgumentException("Data type is INVALID, and not supported");
            }

            //setting fields
            for(int i = 0; i < numberOfFields; i++){
                //todo find a better solution
                switch (dataFields.get(i).getDataType()) {
                    case CHAR:
                        statement.setString(i + 2, (String) dataFields.get(i).getValue());
                        break;
                    case TEXT:
                        statement.setString(i + 2, (String) dataFields.get(i).getValue());
                        break;
                    case BIT:
                        statement.setBoolean(i + 2, (boolean) dataFields.get(i).getValue());
                        break;
                    case NUMERIC:
                        statement.setBigDecimal(i + 2, (BigDecimal) dataFields.get(i).getValue());
                        break;
                    case TINYINT:
                        statement.setByte(i + 2, (byte) dataFields.get(i).getValue());
                        break;
                    case SMALLINT:
                        statement.setShort(i + 2, (short) dataFields.get(i).getValue());
                        break;
                    case INTEGER:
                        statement.setInt(i + 2, (int) dataFields.get(i).getValue());
                        break;
                    case BIGINT:
                        statement.setLong(i + 2, (long) dataFields.get(i).getValue());
                        break;
                    case REAL:
                        statement.setFloat(i + 2, (float) dataFields.get(i).getValue());
                        break;
                    case DOUBLE:
                        statement.setDouble(i + 2, (double) dataFields.get(i).getValue());
                        break;
                    case DATE:
                        statement.setDate(i + 2, (Date) dataFields.get(i).getValue());
                        break;
                    case TIME:
                        statement.setTime(i + 2, (Time) dataFields.get(i).getValue());
                        break;
                    case TIMESTAMP:
                        statement.setTimestamp(i + 2, (Timestamp) dataFields.get(i).getValue());
                        break;
                    case INVALID:
                        throw new IllegalArgumentException("Data type is INVALID, and not supported");
                }
            }

            //setting references
            for(int i = 0; i < numberOfReferences; i++){
                //todo find a more elegent solution
                switch (references.get(i).getREFERENCES_ID().getDataType()) {
                    case CHAR:
                        statement.setString(i+2+numberOfFields, (String) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case TEXT:
                        statement.setString(i+2+numberOfFields, (String) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case BIT:
                        statement.setBoolean(i+2+numberOfFields, (boolean) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case NUMERIC:
                        statement.setBigDecimal(i+2+numberOfFields, (BigDecimal) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case TINYINT:
                        statement.setByte(i+2+numberOfFields, (byte) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case SMALLINT:
                        statement.setShort(i+2+numberOfFields, (short) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case INTEGER:
                        statement.setInt(i+2+numberOfFields, (int) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case BIGINT:
                        statement.setLong(i+2+numberOfFields, (long) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case REAL:
                        statement.setFloat(i+2+numberOfFields, (float) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case DOUBLE:
                        statement.setDouble(i+2+numberOfFields, (double) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case DATE:
                        statement.setDate(i+2+numberOfFields, (Date) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case TIME:
                        statement.setTime(i+2+numberOfFields, (Time) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case TIMESTAMP:
                        statement.setTimestamp(i+2+numberOfFields, (Timestamp) references.get(i).getREFERENCES_ID().getValue());
                        break;
                    case INVALID:
                        throw new IllegalArgumentException("Data type is INVALID, and not supported");
                }
            }
            return statement.executeUpdate() > 0;
        } catch (SQLException e){
            //todo remove stack track
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Creates one or several tables
     * <p>
     *     Gathers required information like field names,
     *     their data types, primary key information,
     *     and reference fields, their types, and what they reference.
     *     Then creates required foreign key tables first
     * </p>
     * @param object The object for which a table is being created
     * @param <C> The type of the object being
     * @return Returns true if the table(s) was created successfully, false if not
     * @throws SORMAccessException Thrown when an object's fields, ID, or references could not be accessed
     */
    private <C> boolean createTable(C object) throws SORMAccessException {
        //Gathering information needed to create a table
        DataField<?> id = POJOPropertyGetSet.getID(object);
        List<DataField<Object>> dataFields = POJOPropertyGetSet.getFields(object);
        List<DataReference<Object>> references = POJOPropertyGetSet.getReference(object);

        //recursive call to create reference / foreign keys first to reference them later on
        for (DataReference<Object> ref:references) {
            if (!objectTableExists(ref.getClass())) {
                if(!createTable(ref.getREFERENCE()))
                    return false;
            }
        }

        //constructing SQL statement
        StringBuilder stringBuilder = new StringBuilder();

        //defining name of table as name of object's class
        stringBuilder.append("create table if not exists " + object.getClass().getSimpleName() + "(");

        //adding primary key as first entry
        stringBuilder.append(id.getValueFieldName() + " " + id.getDataType().toString() + " primary key, ");

        //adding data fields if any exist
        for (DataField<?> field : dataFields) {
            stringBuilder.append(field.getValueFieldName() + " " + field.getDataType().toString() + ", ");
        }

        //adding reference / foreign key fields if any exist
        for (DataReference<Object> ref:references) {
            stringBuilder.append(ref.getREFERENCE_NAME() +
                    " " +
                    ref.getREFERENCES_ID().getDataType().toString() +
                    " references " +
                    ref.getREFERENCE().getClass().getSimpleName() +
                    "(" +
                    ref.getREFERENCES_ID().getValueFieldName() +
                    ") on delete cascade, ");
        }

        //adding field for the serialization of this object
        stringBuilder.setLength(stringBuilder.length() - 2);
        stringBuilder.append(")");

        //todo remobe bread
//        System.out.println("String builder in create table");
//        System.out.println(stringBuilder);

        //executing sql statement to create table
        try(PreparedStatement statement = CONNECTION.prepareStatement(stringBuilder.toString())) {
            statement.execute();
            return true;
        } catch (Exception e){
            //todo remove stack trace
            e.printStackTrace();
        }
        return false;
    }
}
