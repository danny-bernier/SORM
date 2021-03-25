package dev.service.thread;

import dev.database.SORMDAO;
import dev.model.exception.SORMAccessException;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//todo finish this with thread pooling
public enum DBThreadService implements AutoCloseable{
    INSTANCE;

    private DBThreadService(){}

    private final int THREAD_POOL_SIZE = 4;
    ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    /**
     *
     * @param id
     * @param tClass
     * @param iClass
     * @param <T>
     * @param <I>
     * @return
     */
    public <T, I> Future<Optional<T>> getByID(I id, Class<T> tClass, Class<I> iClass){
        return executorService.submit(() -> {
            return new SORMDAO<T,I>(tClass, iClass).getById(id);
        });
    }

    /**
     *
     * @param object
     * @param tClass
     * @param iClass
     * @param <T>
     * @param <I>
     * @return
     */
    public <T, I> Future<Boolean> create(T object, Class<T> tClass, Class<I> iClass){
        return executorService.submit(() -> {
            return new SORMDAO<T,I>(tClass, iClass).create(object);
        });
    }

    /**
     *
     * @param object
     * @param tClass
     * @param iClass
     * @param <T>
     * @param <I>
     * @return
     */
    public <T, I> Future<Boolean> update(T object, Class<T> tClass, Class<I> iClass){
        return executorService.submit(() -> {
            return new SORMDAO<T,I>(tClass, iClass).update(object);
        });
    }

    /**
     *
     * @param object
     * @param tClass
     * @param iClass
     * @param <T>
     * @param <I>
     */
    public <T, I> void delete(T object, Class<T> tClass, Class<I> iClass){
        executorService.submit(() -> {
            try {
                new SORMDAO<T,I>(tClass, iClass).delete(object);
            } catch (SORMAccessException e) {
                //todo replace stack with log?
                e.printStackTrace();
            } catch (SQLException throwables) {
                //todo replace stack with log?
                throwables.printStackTrace();
            }
        });
    }

    @Override
    public void close() throws Exception {
        executorService.shutdown();
        boolean closed = executorService.awaitTermination(3, TimeUnit.SECONDS);
        if(!closed) {
            executorService.shutdownNow();
        }
    }
}
