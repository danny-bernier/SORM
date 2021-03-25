package dev;

import dev.service.thread.DBThreadService;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Primary Class SimpleORM (SORM)
 * <p>This is the primary way for users of this
 * framework to interact with and utilize its features</p>
 */
public class SORM implements AutoCloseable{

    private final DBThreadService THREAD_SERVICE;

    private SORM(){
        this.THREAD_SERVICE = DBThreadService.createDBThreadService();
    }

    /**
     * Creates a new SORM object
     * @return Returns a SORM object
     */
    public static SORM createSORM(){
        return new SORM();
    }

    /**
     * Get object from database by ID, runs in separate thread
     * @param id The id (primary key) marked with {@link dev.model.annotation.SORMID} of the object being retrieved
     * @param tClass The class of the object being retrieved
     * @param iClass The class of the id (primary key) field
     * @param <T> The type of object being retrieved
     * @param <I> The type of id
     * @return Returns a future of optional of the object being retrieved from the database
     */
    public <T, I> Future<Optional<T>> getByID(I id, Class<T> tClass, Class<I> iClass){
        return THREAD_SERVICE.getByID(id, tClass, iClass);
    }

    /**
     * Creates one or more objects/tables in the database
     * @param object The object marked with {@link dev.model.annotation.SORMObject} being stored in the database
     * @param tClass The class of the object being retrieved
     * @param iClass The class of the id (primary key) field
     * @param <T> The type of object being retrieved
     * @param <I> The type of id
     * @return Returns a Future boolean which is true if the object was created successfully, false if not
     */
    public <T, I> Future<Boolean> create(T object, Class<T> tClass, Class<I> iClass){
        return THREAD_SERVICE.create(object, tClass, iClass);
    }

    /**
     * Updates an object in the database, changing its values to reflect the object parameter
     * @param object The object marked with {@link dev.model.annotation.SORMObject} being updated in the database
     * @param tClass The class of the object being retrieved
     * @param iClass The class of the id (primary key) field
     * @param <T> The type of object being retrieved
     * @param <I> The type of id
     * @return Returns a Future boolean which is true if the object was updated successfully, false if not
     */
    public <T, I> Future<Boolean> update(T object, Class<T> tClass, Class<I> iClass){
        return THREAD_SERVICE.update(object, tClass, iClass);
    }

    /**
     * Deletes an object from the database
     * @param object The object marked with {@link dev.model.annotation.SORMObject} being deleted from the database
     * @param tClass The class of the object being retrieved
     * @param iClass The class of the id (primary key) field
     * @param <T> The type of object being retrieved
     * @param <I> The type of id
     */
    public <T, I> Future<Boolean> delete(T object, Class<T> tClass, Class<I> iClass){
        return THREAD_SERVICE.delete(object, tClass, iClass);
    }

    /**
     * Closes thread service
     * @throws Exception Thrown when threads get interrupted
     */
    @Override
    public void close() throws Exception {
        THREAD_SERVICE.close();
    }
}
