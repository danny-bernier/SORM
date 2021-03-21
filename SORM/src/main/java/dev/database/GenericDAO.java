package dev.database;

import dev.model.exception.ConstraintViolationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @param <T> The type of object on which CRUD operations will be performed
 * @param <I> The type of the id (primary key) of object <T>
 */
public class GenericDAO<T, I> implements DAO<T, I>{

    private Connection connection;

    public GenericDAO() throws SQLException{
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public Optional<T> getById(I id) {
        return Optional.empty();
    }

    @Override
    public boolean create(T object) throws ConstraintViolationException {
        return false;
    }

    @Override
    public boolean update(T object) throws ConstraintViolationException {
        return false;
    }

    @Override
    public void delete(T object) {

    }
}
