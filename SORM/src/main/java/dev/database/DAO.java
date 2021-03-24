package dev.database;

import java.util.Optional;

/**
 * Basic interface for all data access objects (DAO)
 * <p>Defines basic CRUD operations (create, read, update, delete)</p>
 * @param <T> the object representation of the database entry.
 * @param <I> the data type of the id (or primary key) field for the object being retrieved.
 */
public interface DAO<T, I> {

    /**
     * Returns an optional of an object representation of a database entry retrieved by that object's ID (or primary key)
     * <p>If query found no entries with specified id, should return an empty optional</p>
     * @return an optional of the object created from the database entry
     * @param id id of the object being retrieved from the database
     */
    Optional<T> getById(I id) throws Exception;

    /**
     * Creates a database entry representing an object
     * @param object The object being added to the database
     * @return If object was successfully added to the database
     */
    boolean create(T object) throws Exception;

    /**
     * Updates a database entry representing the changed object
     * @param object The object being updated in the database
     * @return If object was successfully updated in the database
     */
    boolean update(T object) throws Exception;

    /**
     * Deletes a database entry representing an object
     * @param object The object being deleted from the database
     */
    void delete(T object) throws Exception;
}
