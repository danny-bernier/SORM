package dev.utility.reflection;

import dev.database.SORMDAO;
import dev.model.annotation.*;
import dev.model.database.DataField;
import dev.model.database.DataReference;
import dev.model.exception.NoSORMIDFoundException;
import dev.model.exception.NoSORMObjectFoundException;
import dev.model.exception.SORMAccessException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The PropertyGatherer does the gathering and collecting of properties related to objects using SORM annotations
 *  <p>
 *      {@link SORMObject} to mark class to allow basic CRUD operations
 *      {@link SORMID} to mark property as ID (primary key)
 *      {@link SORMField} to mark property as a field to be stored in the database
 *      {@link SORMReference} to mark an object as a foreign key (has-a relationship)
 *  </p>
 */
public class POJOPropertyGetSet {

    /**
     * Gathers list of DataFields of object marked by {@link SORMField}
     * @param o The object who's DataFields marked with {@link SORMField} will be gathered
     * @return Returns a list of DataField objects
     * @throws SORMAccessException Thrown when a field could not be accessed
     * @throws NoSORMObjectFoundException Thrown when the Object parameter does not have a {@link SORMObject} annotation on its class definition
     */
    public static List<DataField<Object>> getFields (Object o) throws SORMAccessException, NoSORMObjectFoundException {
        if(!o.getClass().isAnnotationPresent(SORMObject.class))
            throw new NoSORMObjectFoundException("No @SORMObject annotation found, ensure classes to be stored are marked with @SORMObject");
        List<DataField<Object>> dataFields = new ArrayList<>();
        try {
            for (Field f : o.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(SORMField.class)) {
                    dataFields.add(DataField.createDataField(f.get(o), f.getName()));
                }
            }
        } catch (IllegalAccessException e){throw new SORMAccessException("One or more fields in " + o.getClass().getSimpleName() + " could not be read, ensure annotated field is accessible and of a supported datatype");}
        return dataFields;
    }

    /**
     * Gathers ID (primary key) of object marked by {@link SORMID}
     * @param o The object who's ID marked with {@link SORMID} will be gathered
     * @return Returns a DataField of the object's {@link SORMID} annotated property
     * @throws NoSORMIDFoundException Thrown when the ID could not be accessed
     * @throws NoSORMObjectFoundException Thrown when the Object parameter does not have a {@link SORMObject} annotation on its class definition
     * @throws SORMAccessException Thrown when an ID field could not be accessed
     */
    public static DataField<Object> getID (Object o) throws NoSORMIDFoundException, NoSORMObjectFoundException, SORMAccessException {
        if(!o.getClass().isAnnotationPresent(SORMObject.class))
            throw new NoSORMObjectFoundException("No @SORMObject annotation found, ensure classes to be stored are marked with @SORMObject");
        try {
            for (Field f : o.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(SORMID.class)) {
                    return DataField.createDataField(f.get(o), f.getName());
                }
            }
        }catch (IllegalAccessException ignored) {throw new SORMAccessException("One or more fields in " + o.getClass().getSimpleName() + " could not be read, ensure annotated field is accessible and of a supported datatype");}
        throw new NoSORMIDFoundException("No appropriate @SORMID annotation was found, ensure this data field is present and accessible, and that is is of a supported datatype");
    }

    /**
     * Gets the name of the field with {@link SORMID}
     * @param clazz The class who's ID name will be retrieved
     * @return Returns the name of the ID field as String
     * @throws NoSORMIDFoundException  Thrown when the ID could not be accessed/does not exist
     * @throws NoSORMObjectFoundException Thrown when the Object parameter does not have a {@link SORMObject} annotation on its class definition
     */
    public static String getIDNameByClass (Class clazz) throws NoSORMObjectFoundException {
        if(!clazz.isAnnotationPresent(SORMObject.class))
            throw new NoSORMObjectFoundException("No @SORMObject annotation found, ensure classes to be stored are marked with @SORMObject");
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(SORMID.class))
                return f.getName();
        }
        throw new NoSORMIDFoundException("No appropriate @SORMID annotation was found, ensure this data field is present and accessible, and that is is of a supported datatype");
    }

    /**
     * Gathers reference objects (foreign keys) marked with {@link SORMReference}
     * @param o The object who's properties marked with {@link SORMReference} will be gathered
     * @return Returns a list of objects marked with {@link SORMReference}, returns an empty list if no appropriate annotations were found
     * @throws NoSORMObjectFoundException Thrown when the Object parameter does not have a {@link SORMObject} annotation on its class definition
     * @throws SORMAccessException Thrown when a field could not be accessed
     */
    public static List<DataReference<Object>> getReference (Object o) throws NoSORMObjectFoundException, SORMAccessException {
        if(!o.getClass().isAnnotationPresent(SORMObject.class))
            throw new NoSORMObjectFoundException("No @SORMObject annotation found, ensure classes to be stored are marked with @SORMObject");
        List<DataReference<Object>> references = new ArrayList<>();
        try {
            for (Field f : o.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(SORMReference.class)) {
                    references.add(DataReference.createDataReference(f.get(o),
                            f.getName(),
                            getID(f.get(o))));
                }
            }
        }catch (IllegalAccessException ignored) {throw new SORMAccessException("One or more fields in " + o.getClass().getSimpleName() + " could not be read, ensure annotated field is accessible and of a supported datatype");}
        return references;
    }

    /**
     * Constructs an object of provided type using provided ResultSet
     * @param id The id of the object being created
     * @param resultSet The ResultSet of object information
     * @param clazz The class of the object being created
     * @param <T> The type of the object being created
     * @param <I> The type of the object's ID
     * @return Returns a created object with fields equal to those in the ResultSet
     * @throws SQLException Thrown when unable to read ResultSet
     * @throws NoSuchFieldException Thrown if field in object being created does not exist
     * @throws IllegalAccessException Thrown when this could not access a field/property of an object
     * @throws InvocationTargetException Thrown when failed to invoke constructor
     * @throws InstantiationException Thrown when the object could not be successfully instantiated
     */
    public static <T, I> Optional<T> buildObject (I id, ResultSet resultSet, Class<T> clazz) throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {

        resultSet.next();

        String idFieldName = POJOPropertyGetSet.getIDNameByClass(clazz);
        DataField<Object> idDataField = DataField.createDataField(id, idFieldName);

        //Creating Object
        Constructor[] constructors = clazz.getDeclaredConstructors();
        Constructor noArg;
        for (Constructor c:constructors) {
            if (c.isAnnotationPresent(SORMNoArgConstructor.class)) {

                c.setAccessible(true);
                T object = clazz.cast(c.newInstance());

                idDataField = POJOPropertyGetSet.getID(object);
                List<DataField<Object>> fields = POJOPropertyGetSet.getFields(object);
                List<DataReference<Object>> references = POJOPropertyGetSet.getReference(object);

                Field idField = clazz.getDeclaredField(idDataField.getValueFieldName());
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
                    Field f = clazz.getDeclaredField(fields.get(i).getValueFieldName());
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
                    Field f = clazz.getDeclaredField(references.get(i).getREFERENCE_NAME());
                    f.setAccessible(true);

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
                return Optional.of(clazz.cast(object));
            }
        }
        return Optional.empty();
    }
}
