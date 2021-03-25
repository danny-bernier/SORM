package dev.service.thread;

import dev.database.SORMDAO;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DBThreadService implements AutoCloseable{

    private DBThreadService(){}

    /**
     * Creates a new instance of DBThreadService
     * @return Returns DBThreadService
     */
    public static DBThreadService createDBThreadService(){
        return new DBThreadService();
    }

    private final int THREAD_POOL_SIZE = 4;
    ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

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
        return executorService.submit(() -> {
            return new SORMDAO<T,I>(tClass, iClass).getById(id);
        });
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
        return executorService.submit(() -> {
            return new SORMDAO<T,I>(tClass, iClass).create(object);
        });
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
        return executorService.submit(() -> {
            return new SORMDAO<T,I>(tClass, iClass).update(object);
        });
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
        return executorService.submit(() -> {
            return new SORMDAO<T,I>(tClass, iClass).delete(object);
        });
    }

    /**
     * Autocloseable implementation
     * <p>
     *     Gives currently running threads 3 seconds to finish execution before forcibly closing them.
     * </p>
     * @throws Exception Thrown when forcibly closing/interrupting threads
     */
    @Override
    public void close() throws Exception {
        executorService.shutdown();
        boolean closed = executorService.awaitTermination(3, TimeUnit.SECONDS);
        if(!closed) {executorService.shutdownNow();}
    }
}
