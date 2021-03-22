package dev.database;

import dev.model.database.DataField;
import dev.model.database.DataReference;
import dev.model.exception.ConstraintViolationException;
import dev.model.exception.SORMAccessException;
import dev.utility.reflection.PropertyGatherer;
import net.jodah.typetools.TypeResolver;
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

    public SORMDAO() throws SQLException{
        this.CONNECTION = DBConnection.getInstance().getConnection();
        Class<?>[] typeArguments = TypeResolver.resolveRawArguments(SORMDAO.class, getClass());
        this.OBJECT_T_CLASS = (Class<T>) typeArguments[0];
        this.ID_I_CLASS = (Class<I>) typeArguments[1];
    }

    @Override
    public Optional<T> getById(I id) {
        if(objectTableExists(OBJECT_T_CLASS)){

        }
        return Optional.empty();
    }

    @Override
    public boolean create(T object) throws ConstraintViolationException, SORMAccessException {

        if(createTable(object))
            return addObject(object);

        return false;
    }

    @Override
    public boolean update(T object) throws ConstraintViolationException {
        return false;
    }

    @Override
    public void delete(T object) {

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
        DataField<Object> id = PropertyGatherer.getID(object);
        List<DataField<Object>> dataFields = PropertyGatherer.getFields(object);
        List<DataReference<Object>> references = PropertyGatherer.getReference(object);

        //counts of fields and references for use later in constructing statement
        int numberOfFields = 0;
        int numberOfReferences = 0;

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

        //removing trailing ", " caused by unknown amount of fields and references
        stringBuilder.setLength(stringBuilder.length() - 2);

        //closing out sql statement
        stringBuilder.append(")");


        //todo fix set
        //executing sql statement to create table
        try(PreparedStatement statement = CONNECTION.prepareStatement(stringBuilder.toString())) {
            //setting id (primary key)
            statement.setObject(1, id.getValue());

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
                }
            }

            return statement.executeUpdate() > 0;
        } catch (SQLException e){e.printStackTrace();}
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
     * @param object
     * @param <C>
     * @return
     * @throws SORMAccessException
     */
    private <C> boolean createTable(C object) throws SORMAccessException {
        //Gathering information needed to create a table
        DataField<?> id = PropertyGatherer.getID(object);
        List<DataField<Object>> dataFields = PropertyGatherer.getFields(object);
        List<DataReference<Object>> references = PropertyGatherer.getReference(object);

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
                    "), ");
        }

        //removing trailing ", " caused by unknown amount of fields and references
        stringBuilder.setLength(stringBuilder.length() - 2);

        //closing out sql statement
        stringBuilder.append(")");

        //executing sql statement to create table
        try(PreparedStatement statement = CONNECTION.prepareStatement(stringBuilder.toString())) {
            statement.execute();
            return true;
        } catch (SQLException e){e.printStackTrace();}
        return false;
    }
}
