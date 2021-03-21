package dev.database;

import dev.model.database.DataField;
import dev.model.database.DataReference;
import dev.model.exception.ConstraintViolationException;
import dev.model.exception.SORMAccessException;
import dev.utility.reflection.PropertyGatherer;
import net.jodah.typetools.TypeResolver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 *
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
        List<DataField<?>> dataFields = PropertyGatherer.getFields(object);
        DataField<?> id = PropertyGatherer.getID(object);

        //todo remove this?
        createTable(object);

        //todo add object to table
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
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("select 1 from " + clazz.getSimpleName());
            statement.executeQuery();
            return true;
        } catch (SQLException ignored){
            return  false;
        }
    }

    private <C> boolean createTable(C object) throws SORMAccessException {
        DataField<?> id = PropertyGatherer.getID(object);
        List<DataField<?>> dataFields = PropertyGatherer.getFields(object);
        List<DataReference<?>> references = PropertyGatherer.getReference(object);

        for (DataReference ref:references) {
            if (!objectTableExists(ref.getClass())) {
                createTable(ref.getREFERENCE());
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("create table if not exists " + object.getClass().getSimpleName() + "(");
        stringBuilder.append(id.getValueFieldName() + " " + id.getDataType().toString() + " primary key, ");

        for (DataField<?> field : dataFields) {
            stringBuilder.append(field.getValueFieldName() + " " + field.getDataType().toString() + ", ");
        }

        for (DataReference ref:references) {
            stringBuilder.append(ref.getREFERENCE_NAME() +
                    " " +
                    ref.getREFERENCES_ID().getDataType().toString() +
                    " references " +
                    ref.getREFERENCE().getClass().getSimpleName() +
                    "(" +
                    ref.getREFERENCES_ID().getValueFieldName() +
                    "), ");
        }

        stringBuilder.setLength(stringBuilder.length() - 2);
        stringBuilder.append(")");

        try {
            PreparedStatement statement = CONNECTION.prepareStatement(stringBuilder.toString());
        } catch (SQLException e){
            e.printStackTrace();
        }

        return true;
    }
}
